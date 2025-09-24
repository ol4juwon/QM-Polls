package com.olajuwon.qm.vote_publisher.Service.impl;

import com.olajuwon.qm.vote_publisher.DTOs.VoteDto;
import com.olajuwon.qm.vote_publisher.Entity.Poll;
import com.olajuwon.qm.vote_publisher.Service.VoteService;
import com.olajuwon.qm.vote_publisher.utils.ApiResponse;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.olajuwon.qm.vote_publisher.config.RabbitMQConfig.EXCHANGE;
import static com.olajuwon.qm.vote_publisher.config.RabbitMQConfig.ROUTING_KEY;
import static com.olajuwon.qm.vote_publisher.utils.ResponseUtils.createFailureResponse;
import static com.olajuwon.qm.vote_publisher.utils.ResponseUtils.createSuccessResponse;

@Service
@Slf4j
public class VoteServiceImpl implements VoteService {

    private final RabbitTemplate rabbit;
    private final Logger logger = LoggerFactory.getLogger(VoteServiceImpl.class);
    private final StringRedisTemplate redis;
    private final WebClient pollClient;
    private final Duration IDEMPOTENCY_TTL = Duration.ofSeconds(15);

    public VoteServiceImpl(RabbitTemplate rabbit, StringRedisTemplate redis, @Value("${POLL_SVC_URL:http://localhost:5004/api/v1}") String pollurl) {
        this.rabbit = rabbit;
        this.redis = redis;
        this.pollClient = WebClient.create(pollurl);
    }

    @Override
    public ResponseEntity<ApiResponse<?>> vote(String pollId, VoteDto.Request request) {
        String user = request.getUserId();
        String option = request.getOption();
        String idem = request.getIdempotencyKey();
        logger.info("user: {}, pollId: {}, option: {}, idempotencyKey: {}", user, pollId, option, idem);
        // idempotency key: if client didn't provide, generate one from user+poll
        if (idem == null || idem.isBlank()) {
            idem = UUID.nameUUIDFromBytes((user + ":" + pollId + ":" + option).getBytes()).toString();
        }

        String redisKey = "vote:idem:" + idem;
        Boolean exists = redis.hasKey(redisKey);
        if (Boolean.TRUE.equals(exists)) {
            return ResponseEntity.status(409).body(createFailureResponse(null, "duplicate response"));
        }

        ParameterizedTypeReference<ApiResponse<Poll>> responseType =
                new ParameterizedTypeReference<>() {
                };
        // validate poll exists and option valid
        ApiResponse<Poll> response = pollClient.get().uri("/polls/{id}", pollId).retrieve().bodyToMono(responseType).block();
        if (response == null) return ResponseEntity.notFound().build();
        if(response.getData() == null)
        {
            return ResponseEntity.notFound().build();
        }
        Poll poll = Poll.builder()
                .id(response.getData().getId())
                .question(response.getData().getQuestion())
                .options(response.getData().getOptions().stream().toList())
                .scheduledEndTime(response.getData().getScheduledEndTime())
                .scheduledStartTime(response.getData().getScheduledStartTime())
                .build();
        if (poll == null) return ResponseEntity.notFound().build();
        logger.info("poll found: {}", poll);
        if (poll.getScheduledEndTime().isBefore(java.time.Instant.now()))
            return ResponseEntity.badRequest().body(createFailureResponse(null, "poll has ended"));

        List<String> options = poll.getOptions();
        logger.info("options: {}", options);
        logger.info("option: {}", option);
        if (!options.contains(option))
            return ResponseEntity.badRequest().body(createFailureResponse(null, "invalid option"));


        redis.opsForValue().set(redisKey, "1", IDEMPOTENCY_TTL);

        // prepare event
        Map<String, Object> event = Map.of(
                "eventType", "VoteEvent",
                "eventId", idem,
                "pollId", pollId,
                "userId", user,
                "option", option,
                "clientTimestamp", java.time.Instant.now().toString()
        );
        try {
            rabbit.convertAndSend(EXCHANGE, ROUTING_KEY, event);
            logger.info("event sent: {}", event);
        }catch (Exception e){
            logger.error("error sending event: {}", e.getMessage());
        }
        return ResponseEntity.accepted().body(createSuccessResponse(Map.of("status", "accepted", "idempotencyKey", idem), "Vote received"));
    }
}

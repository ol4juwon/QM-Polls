package com.olajuwon.qm.vote_processor.Listener;

import com.olajuwon.qm.vote_processor.Entity.Vote;
import com.olajuwon.qm.vote_processor.Repository.VoteRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Slf4j
public class VoteListener {

    private static final Logger logger = LoggerFactory.getLogger(VoteListener.class.getName());
    private final VoteRepository repo;
    private final StringRedisTemplate redisTemplate;

    public VoteListener(VoteRepository repo, StringRedisTemplate redisTemplate) {
        this.repo = repo;
        this.redisTemplate = redisTemplate;
    }

    @RabbitListener(queues = "vote-queue")
    @Transactional
    public void handle(Map<String, Object> event) {
        try {
            logger.info("Received event: {}",event);
            // naive mapping
            Vote v = new Vote();
            v.setPollId((String) event.get("pollId"));
            v.setUserId((String) event.get("userId"));
            v.setOption((String) event.get("option"));
            v.setIdempotencyKey((String) event.get("eventId"));

            repo.save(v);
            String key = "poll:" + v.getPollId();
            logger.info("redis key: {}", key);
            try {
                redisTemplate.opsForHash().increment(key, v.getOption(), 1);
            }catch (Exception ex){
                logger.error("error redis: {}", ex.getMessage(), ex);
            }
        } catch (Exception ex) {
            // duplicate or constraint violation -> ignore
            logger.error("duplicate or error: {}", ex.getMessage(), ex);
        }
    }
}

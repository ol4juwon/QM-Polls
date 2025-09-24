package com.olajuwon.qm.polls_service.Service.impl;

import com.olajuwon.qm.polls_service.DTOs.CreatePollDto;
import com.olajuwon.qm.polls_service.DTOs.GetAllPollsDto;
import com.olajuwon.qm.polls_service.DTOs.GetPollDto;
import com.olajuwon.qm.polls_service.Entity.Poll;
import com.olajuwon.qm.polls_service.Repository.PollRepository;
import com.olajuwon.qm.polls_service.Service.PollService;
import com.olajuwon.qm.polls_service.Utils.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.swing.text.html.Option;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.olajuwon.qm.polls_service.Utils.ResponseUtils.createFailureResponse;
import static com.olajuwon.qm.polls_service.Utils.ResponseUtils.createSuccessResponse;


@Service
@Slf4j
@Validated
public class PollServiceImpl implements PollService {

    private static final String TAG = "PollServiceImpl";
    private static final Logger logger = LoggerFactory.getLogger(PollServiceImpl.class);
    @Autowired
    private PollRepository pollRepository;

    private final StringRedisTemplate redisTemplate;

    PollServiceImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public ResponseEntity<ApiResponse<Poll>> createPoll( CreatePollDto.Request request) {
        try {
            Poll poll = Poll.builder()
                    .options(request.getOptions())
                    .question(request.getQuestion())
                    .scheduledEndTime(request.getScheduledEndTime())
                    .scheduledStartTime(request.getScheduledStartTime()).build();
            poll = pollRepository.save(poll);

            return ResponseEntity.status(HttpStatus.CREATED).body(createSuccessResponse(poll, "created"));
        }catch (Exception ex){
            logger.error("{} createPoll Error: {}", TAG, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createFailureResponse(
                    "Poll creation failed",
                    ex.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse<Poll>> getPoll( String pollId) {
        try{
            Optional<Poll> poll = pollRepository.findById(pollId);
            if (poll.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createFailureResponse(
                        "Poll with PollId: %s Not Found".formatted(pollId),
                        "Poll not found"));
            }
            return ResponseEntity.status(HttpStatus.OK).body(createSuccessResponse(poll.get(), "Poll Fecthed"));

        }catch (Exception ex){
            logger.error("{} getPoll Error: {}", TAG, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createFailureResponse(
                    "Poll with PollId: %s Fetch failed".formatted(pollId),
                    ex.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse<GetAllPollsDto.Response>> getAllPoll(GetAllPollsDto.Request getAllFilter) {
        Sort.Direction direction = getAllFilter.getOrder().equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sortBy = Sort.by(direction, getAllFilter.getSort());
        Pageable pageable = PageRequest.of(getAllFilter.getPage(), getAllFilter.getLimit(), sortBy);

        Page<Poll> polls = pollRepository.findAll(pageable);
        if (polls.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(createSuccessResponse(null, "No Polls Found"));
        }
        GetAllPollsDto.Response response = new GetAllPollsDto.Response();
        response.setPolls(polls.getContent());
        response.setPage(getAllFilter.getPage());
        response.setTotal(polls.getTotalElements());
        return ResponseEntity.status(HttpStatus.OK).body(createSuccessResponse(response, "Polls Fetched"));
    }
}

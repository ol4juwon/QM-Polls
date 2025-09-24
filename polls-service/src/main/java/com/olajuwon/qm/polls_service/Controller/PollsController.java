package com.olajuwon.qm.polls_service.Controller;


import com.olajuwon.qm.polls_service.DTOs.CreatePollDto;
import com.olajuwon.qm.polls_service.DTOs.GetAllPollsDto;
import com.olajuwon.qm.polls_service.DTOs.GetPollDto;
import com.olajuwon.qm.polls_service.Entity.Poll;
import com.olajuwon.qm.polls_service.Service.PollService;
import com.olajuwon.qm.polls_service.Utils.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/polls")
@Validated
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
@Slf4j
public class PollsController {
    @Autowired
    PollService pollService;

    /**
     * Create Poll
     * @param request
     * @return
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Poll>> create(@Valid @RequestBody CreatePollDto.Request request){
        log.info("create poll: {}", request);
        return pollService.createPoll(request);
    }

    /**
     * get single poll
     * @param pollId
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Poll>> get(@Valid @PathVariable("id") String pollId){
        return pollService.getPoll(pollId);
    }

    /**
     *
     * @param
     * @return
     */
    @GetMapping("/")
    public ResponseEntity<ApiResponse<GetAllPollsDto.Response>> getAll(GetAllPollsDto.Request getAllFilter){
        log.info("get all polls:");
        return pollService.getAllPoll(getAllFilter);
    }
}

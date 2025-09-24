package com.olajuwon.qm.vote_publisher.Controller;

import com.olajuwon.qm.vote_publisher.DTOs.VoteDto;
import com.olajuwon.qm.vote_publisher.Service.VoteService;
import com.olajuwon.qm.vote_publisher.utils.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class VoteController {

    @Autowired
    VoteService voteService;

    @PostMapping("/polls/{id}/vote")
    public ResponseEntity<ApiResponse<?>> vote(@PathVariable("id") String pollId,@Valid @RequestBody VoteDto.Request request) {
        return voteService.vote(pollId, request);
    }
}

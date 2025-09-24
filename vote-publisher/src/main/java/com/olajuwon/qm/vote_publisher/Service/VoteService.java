package com.olajuwon.qm.vote_publisher.Service;

import com.olajuwon.qm.vote_publisher.DTOs.VoteDto;
import com.olajuwon.qm.vote_publisher.utils.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface VoteService {

    public ResponseEntity<ApiResponse<?>> vote(String pollId, VoteDto.Request request);
}

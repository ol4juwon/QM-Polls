package com.olajuwon.qm.polls_service.Service;

import com.olajuwon.qm.polls_service.DTOs.CreatePollDto;
import com.olajuwon.qm.polls_service.DTOs.GetAllPollsDto;
import com.olajuwon.qm.polls_service.DTOs.GetPollDto;
import com.olajuwon.qm.polls_service.Entity.Poll;
import com.olajuwon.qm.polls_service.Utils.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

public interface PollService {
    ResponseEntity<ApiResponse<Poll>> createPoll( CreatePollDto.Request request);
    ResponseEntity<ApiResponse<Poll>> getPoll(String pollId);
    ResponseEntity<ApiResponse<GetAllPollsDto.Response>> getAllPoll(GetAllPollsDto.Request getAllFilter);
}

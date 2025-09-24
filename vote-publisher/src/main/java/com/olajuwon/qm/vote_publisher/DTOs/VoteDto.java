package com.olajuwon.qm.vote_publisher.DTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class VoteDto {

    @Getter
    @Setter
    @ToString
    public static class Request {
        @NotBlank(message= "User Id is required")
        String userId;
        @NotBlank(message= "Option is required")
        String option;

        String idempotencyKey;
    }
}

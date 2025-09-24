package com.olajuwon.qm.polls_service.DTOs;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

import java.time.Instant;
import java.util.List;


public class CreatePollDto {

    @Getter
    @Setter
    @ToString
    @Validated
    public static class Request {
        @NotNull(message = "question is required")
        private String question;
        @NotEmpty(message = "Options cannot be empty ")
        @Size(min = 4, max = 4, message = "Options must be length 4")
        private List<String> options;

        @NotNull(message = "Scheduled start time is required")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
        private Instant scheduledStartTime;

        @NotNull(message = "Duration must be greater than 0")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
        private Instant scheduledEndTime;

    }

    @Getter
    @Setter
    @ToString
    public static class Response<ApiResponse>{
        private String id;
        private String question;
        private List<String> options;
        private Instant scheduledStartTime;
        private Instant scheduledEndTime;
    }
}

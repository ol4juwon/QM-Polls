package com.olajuwon.qm.polls_service.DTOs;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class GetPollDto {

    @Getter
    @Setter
    @ToString
    public static class Request {
        public String id;
    }

    @Getter
    @Setter
    @ToString
    public static class Response {
        private String id;
        private String question;
        private List<String> options;
        private Instant scheduledStartTime;
        private Instant scheduledEndTime;
    }

}

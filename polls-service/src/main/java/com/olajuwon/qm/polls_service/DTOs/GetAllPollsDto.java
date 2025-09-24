package com.olajuwon.qm.polls_service.DTOs;

import com.olajuwon.qm.polls_service.Entity.Poll;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

public class GetAllPollsDto {

    @Getter
    @Setter
    @ToString
    public static class Request {
        private int limit = 10;
        private int page = 1;
        private String sort = "scheduledStartTime";
        private String order = "asc";
    }
    @Getter
    @Setter
    @ToString
    public static class Response {
        private long total;
        private int page;
        private int pageSize;
        private List<Poll> polls;
    }
}

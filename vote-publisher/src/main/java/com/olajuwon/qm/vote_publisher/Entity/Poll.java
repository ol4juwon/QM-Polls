package com.olajuwon.qm.vote_publisher.Entity;

import lombok.*;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Poll {
    private String id ;

    private String question;

    private List<String> options;

    private Instant scheduledStartTime;

    private Instant scheduledEndTime;

}
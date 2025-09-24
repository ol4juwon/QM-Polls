package com.olajuwon.qm.socket_server.Entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;


@Table(name = "polls")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Poll {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id ;

    @Column(nullable = false)
    private String question;

    @ElementCollection
    @Column(name = "options")
    private List<String> options;

    @Column(nullable = false)
    private Instant scheduledStartTime;

    @Column(nullable = false)
    private Instant scheduledEndTime;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean closed;

}

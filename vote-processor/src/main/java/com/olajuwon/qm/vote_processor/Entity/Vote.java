    package com.olajuwon.qm.vote_processor.Entity;

    import jakarta.persistence.*;
    import lombok.*;

    import java.time.Instant;
    import java.util.UUID;

    @Entity
    @Table(name = "votes", uniqueConstraints = {
        @UniqueConstraint(name = "uk_vote_idem", columnNames = {"polls_id","user_id","idempotency_key"})
    }

    )
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public class Vote {
        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private String id ;

        @Column(name = "polls_id", nullable = false)
        private String pollId;

        @Column(nullable = false)
        private String userId;

        @Column(nullable = false)
        private String option;

        @Column(name = "idempotency_key", nullable = false)
        private String idempotencyKey;

        @Column(nullable = false)
        private Instant timestamp = Instant.now();

    }

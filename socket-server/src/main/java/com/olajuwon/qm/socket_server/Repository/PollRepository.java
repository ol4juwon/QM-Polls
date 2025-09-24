package com.olajuwon.qm.socket_server.Repository;

import com.olajuwon.qm.socket_server.Entity.Poll;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface PollRepository extends JpaRepository<Poll, String> {
    List<Poll> findByScheduledEndTimeBeforeAndClosedFalse(Instant now);
}

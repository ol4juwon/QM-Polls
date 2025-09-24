package com.olajuwon.qm.polls_service.Repository;


import com.olajuwon.qm.polls_service.Entity.Poll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
@Repository
public interface PollRepository extends JpaRepository<Poll, String> {
   }

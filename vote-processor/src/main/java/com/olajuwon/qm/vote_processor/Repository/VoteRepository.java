package com.olajuwon.qm.vote_processor.Repository;

import com.olajuwon.qm.vote_processor.Entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VoteRepository extends JpaRepository<Vote, String> {}

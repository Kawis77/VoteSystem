package com.example.votesystem.repository;

import com.example.votesystem.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    boolean existsByVoterIdAndElectionId(Long voterId, Long electionId);

    List<Vote> findByVoterId(Long voterId);
}

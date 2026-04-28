package com.example.votesystem.service;

import com.example.votesystem.entity.Vote;
import com.example.votesystem.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;

    @Transactional(readOnly = true)
    public List<Vote> findAll() {
        return voteRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Vote findById(Long id) {
        return voteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vote not found: " + id));
    }

    @Transactional
    public Vote create(Vote vote) {
        return voteRepository.save(vote);
    }

    @Transactional
    public Vote update(Long id, Vote vote) {
        Vote existing = findById(id);
        existing.setVoterId(vote.getVoterId());
        existing.setElection(vote.getElection());
        existing.setCandidate(vote.getCandidate());
        return voteRepository.save(existing);
    }

    @Transactional
    public void deleteById(Long id) {
        voteRepository.deleteById(id);
    }
}

package com.example.votesystem.service;

import com.example.votesystem.dto.request.VoteRequest;
import com.example.votesystem.dto.response.VoteResponse;
import com.example.votesystem.entity.Candidate;
import com.example.votesystem.entity.Election;
import com.example.votesystem.entity.ElectionStatus;
import com.example.votesystem.entity.Vote;
import com.example.votesystem.entity.Voter;
import com.example.votesystem.mapper.VoteMapper;
import com.example.votesystem.repository.CandidateRepository;
import com.example.votesystem.repository.ElectionRepository;
import com.example.votesystem.repository.VoterRepository;
import com.example.votesystem.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final VoterRepository voterRepository;
    private final ElectionRepository electionRepository;
    private final CandidateRepository candidateRepository;
    private final VoteMapper voteMapper;

    @Transactional(readOnly = true)
    public List<VoteResponse> findAll() {
        return voteRepository.findAll()
                .stream()
                .map(voteMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public VoteResponse findById(Long id) {
        Vote vote = voteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vote not found: " + id));
        return voteMapper.toResponse(vote);
    }

    @Transactional
    public VoteResponse castVote(VoteRequest request) {
        Voter voter = voterRepository.findById(request.voterId())
                .orElseThrow(() -> new IllegalArgumentException("Voter not found: " + request.voterId()));
        if (voter.isBlocked()) {
            throw new IllegalStateException("Blocked voter cannot vote: " + request.voterId());
        }

        Election election = electionRepository.findById(request.electionId())
                .orElseThrow(() -> new IllegalArgumentException("Election not found: " + request.electionId()));
        if (election.getStatus() != ElectionStatus.OPEN) {
            throw new IllegalStateException("Voting is allowed only for OPEN elections: " + request.electionId());
        }

        Candidate candidate = candidateRepository.findById(request.candidateId())
                .orElseThrow(() -> new IllegalArgumentException("Candidate not found: " + request.candidateId()));

        if (!candidate.getElection().getId().equals(request.electionId())) {
            throw new IllegalStateException("Candidate does not belong to election: " + request.electionId());
        }

        boolean alreadyVoted = voteRepository.existsByVoterIdAndElectionId(request.voterId(), request.electionId());
        if (alreadyVoted) {
            throw new IllegalStateException(
                    "Voter has already voted in this election: voterId=" + request.voterId() + ", electionId=" + request.electionId());
        }

        Vote vote = voteMapper.toEntity(request, election, candidate);
        Vote saved = voteRepository.save(vote);
        return voteMapper.toResponse(saved);
    }
}

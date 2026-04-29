package com.example.votesystem.service;

import com.example.votesystem.dto.request.VoteRequest;
import com.example.votesystem.dto.response.VoteByVoterResponse;
import com.example.votesystem.dto.response.VoteResponse;
import com.example.votesystem.entity.Candidate;
import com.example.votesystem.entity.Election;
import com.example.votesystem.entity.ElectionStatus;
import com.example.votesystem.entity.Vote;
import com.example.votesystem.entity.Voter;
import com.example.votesystem.exception.CandidateMismatchException;
import com.example.votesystem.exception.DuplicateVoteException;
import com.example.votesystem.exception.ElectionClosedException;
import com.example.votesystem.exception.NotFoundException;
import com.example.votesystem.exception.VoterBlockedException;
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
                .orElseThrow(() -> new NotFoundException("Vote not found: " + id));
        return voteMapper.toResponse(vote);
    }

    @Transactional(readOnly = true)
    public List<VoteByVoterResponse> findByVoterId(Long voterId) {
        voterRepository.findById(voterId)
                .orElseThrow(() -> new NotFoundException("Voter not found: " + voterId));

        return voteRepository.findByVoterId(voterId)
                .stream()
                .map(voteMapper::toVoteByVoterResponse)
                .toList();
    }

    @Transactional
    public VoteResponse castVote(VoteRequest request) {
        Voter voter = voterRepository.findById(request.voterId())
                .orElseThrow(() -> new NotFoundException("Voter not found: " + request.voterId()));
        if (voter.isBlocked()) {
            throw new VoterBlockedException("Voter with id=" + request.voterId() + " is blocked and cannot cast a vote");
        }

        Election election = electionRepository.findById(request.electionId())
                .orElseThrow(() -> new NotFoundException("Election not found: " + request.electionId()));
        if (election.getStatus() != ElectionStatus.OPEN) {
            throw new ElectionClosedException(
                    "Election with id=" + request.electionId() + " is " + election.getStatus() + " and is not open for voting");
        }

        Candidate candidate = candidateRepository.findById(request.candidateId())
                .orElseThrow(() -> new NotFoundException("Candidate not found: " + request.candidateId()));

        if (!candidate.getElection().getId().equals(request.electionId())) {
            throw new CandidateMismatchException(
                    "Candidate with id=" + request.candidateId() + " belongs to election id="
                            + candidate.getElection().getId() + " but request used election id=" + request.electionId());
        }

        boolean alreadyVoted = voteRepository.existsByVoterIdAndElectionId(request.voterId(), request.electionId());
        if (alreadyVoted) {
            throw new DuplicateVoteException(
                    "Voter with id=" + request.voterId() + " has already cast a vote in election id=" + request.electionId());
        }

        Vote vote = voteMapper.toEntity(request, election, candidate);
        Vote saved = voteRepository.save(vote);
        return voteMapper.toResponse(saved);
    }
}

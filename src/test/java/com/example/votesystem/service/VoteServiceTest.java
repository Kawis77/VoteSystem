package com.example.votesystem.service;

import com.example.votesystem.dto.request.VoteRequest;
import com.example.votesystem.dto.response.VoteResponse;
import com.example.votesystem.entity.Candidate;
import com.example.votesystem.entity.Election;
import com.example.votesystem.entity.ElectionStatus;
import com.example.votesystem.entity.Vote;
import com.example.votesystem.entity.Voter;
import com.example.votesystem.exception.CandidateMismatchException;
import com.example.votesystem.exception.DuplicateVoteException;
import com.example.votesystem.exception.ElectionClosedException;
import com.example.votesystem.exception.VoterBlockedException;
import com.example.votesystem.mapper.VoteMapper;
import com.example.votesystem.repository.CandidateRepository;
import com.example.votesystem.repository.ElectionRepository;
import com.example.votesystem.repository.VoteRepository;
import com.example.votesystem.repository.VoterRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VoteServiceTest {

    @Mock
    private VoteRepository voteRepository;
    @Mock
    private VoterRepository voterRepository;
    @Mock
    private ElectionRepository electionRepository;
    @Mock
    private CandidateRepository candidateRepository;
    @Mock
    private VoteMapper voteMapper;

    @InjectMocks
    private VoteService voteService;

    @Test
    void shouldCastVoteSuccessfully_whenAllRulesAreSatisfied() {
        VoteRequest request = new VoteRequest(1L, 10L, 100L);

        Voter voter = new Voter();
        voter.setId(1L);
        voter.setBlocked(false);

        Election election = new Election();
        election.setId(10L);
        election.setStatus(ElectionStatus.OPEN);

        Candidate candidate = new Candidate();
        candidate.setId(100L);
        candidate.setElection(election);

        Vote vote = new Vote();
        vote.setVoterId(1L);
        vote.setElection(election);
        vote.setCandidate(candidate);

        Vote savedVote = new Vote();
        savedVote.setId(500L);
        savedVote.setVoterId(1L);
        savedVote.setElection(election);
        savedVote.setCandidate(candidate);

        VoteResponse response = new VoteResponse(500L, 1L, 10L, 100L);

        when(voterRepository.findById(1L)).thenReturn(Optional.of(voter));
        when(electionRepository.findById(10L)).thenReturn(Optional.of(election));
        when(candidateRepository.findById(100L)).thenReturn(Optional.of(candidate));
        when(voteRepository.existsByVoterIdAndElectionId(1L, 10L)).thenReturn(false);
        when(voteMapper.toEntity(request, election, candidate)).thenReturn(vote);
        when(voteRepository.save(vote)).thenReturn(savedVote);
        when(voteMapper.toResponse(savedVote)).thenReturn(response);

        VoteResponse result = voteService.castVote(request);

        assertNotNull(result);
        verify(voteRepository).save(vote);
    }

    @Test
    void shouldThrowWhenVoterBlocked() {
        VoteRequest request = new VoteRequest(1L, 10L, 100L);
        Voter voter = new Voter();
        voter.setId(1L);
        voter.setBlocked(true);

        when(voterRepository.findById(1L)).thenReturn(Optional.of(voter));

        assertThrows(VoterBlockedException.class, () -> voteService.castVote(request));
        verify(voteRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenElectionClosed() {
        VoteRequest request = new VoteRequest(1L, 10L, 100L);

        Voter voter = new Voter();
        voter.setId(1L);
        voter.setBlocked(false);

        Election election = new Election();
        election.setId(10L);
        election.setStatus(ElectionStatus.CLOSED);

        when(voterRepository.findById(1L)).thenReturn(Optional.of(voter));
        when(electionRepository.findById(10L)).thenReturn(Optional.of(election));

        assertThrows(ElectionClosedException.class, () -> voteService.castVote(request));
        verify(voteRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenCandidateNotInElection() {
        VoteRequest request = new VoteRequest(1L, 10L, 100L);

        Voter voter = new Voter();
        voter.setId(1L);
        voter.setBlocked(false);

        Election requestElection = new Election();
        requestElection.setId(10L);
        requestElection.setStatus(ElectionStatus.OPEN);

        Election candidateElection = new Election();
        candidateElection.setId(99L);
        candidateElection.setStatus(ElectionStatus.OPEN);

        Candidate candidate = new Candidate();
        candidate.setId(100L);
        candidate.setElection(candidateElection);

        when(voterRepository.findById(1L)).thenReturn(Optional.of(voter));
        when(electionRepository.findById(10L)).thenReturn(Optional.of(requestElection));
        when(candidateRepository.findById(100L)).thenReturn(Optional.of(candidate));

        assertThrows(CandidateMismatchException.class, () -> voteService.castVote(request));
        verify(voteRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenAlreadyVoted() {
        VoteRequest request = new VoteRequest(1L, 10L, 100L);

        Voter voter = new Voter();
        voter.setId(1L);
        voter.setBlocked(false);

        Election election = new Election();
        election.setId(10L);
        election.setStatus(ElectionStatus.OPEN);

        Candidate candidate = new Candidate();
        candidate.setId(100L);
        candidate.setElection(election);

        when(voterRepository.findById(1L)).thenReturn(Optional.of(voter));
        when(electionRepository.findById(10L)).thenReturn(Optional.of(election));
        when(candidateRepository.findById(100L)).thenReturn(Optional.of(candidate));
        when(voteRepository.existsByVoterIdAndElectionId(1L, 10L)).thenReturn(true);

        assertThrows(DuplicateVoteException.class, () -> voteService.castVote(request));
        verify(voteRepository, never()).save(any());
    }
}

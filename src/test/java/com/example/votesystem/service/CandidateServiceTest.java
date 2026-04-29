package com.example.votesystem.service;

import com.example.votesystem.dto.request.CandidateRequest;
import com.example.votesystem.dto.response.CandidateResponse;
import com.example.votesystem.entity.Candidate;
import com.example.votesystem.entity.Election;
import com.example.votesystem.entity.ElectionStatus;
import com.example.votesystem.exception.CandidateElectionChangeNotAllowedException;
import com.example.votesystem.exception.ElectionClosedException;
import com.example.votesystem.mapper.CandidateMapper;
import com.example.votesystem.repository.CandidateRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CandidateServiceTest {

    @Mock
    private CandidateRepository candidateRepository;
    @Mock
    private CandidateMapper candidateMapper;
    @Mock
    private ElectionService electionService;

    @InjectMocks
    private CandidateService candidateService;

    @Test
    void shouldCreateCandidate_whenElectionIsOpen() {
        CandidateRequest request = new CandidateRequest("John", 10L);

        Election election = new Election();
        election.setId(10L);
        election.setStatus(ElectionStatus.OPEN);

        Candidate candidate = new Candidate();
        candidate.setName("John");

        Candidate saved = new Candidate();
        saved.setId(1L);
        saved.setName("John");
        saved.setElection(election);

        CandidateResponse response = new CandidateResponse(1L, "John", 10L);

        when(electionService.findEntityById(10L)).thenReturn(election);
        when(candidateMapper.toEntity(request)).thenReturn(candidate);
        when(candidateRepository.save(candidate)).thenReturn(saved);
        when(candidateMapper.toResponse(saved)).thenReturn(response);

        CandidateResponse result = candidateService.create(request);

        assertNotNull(result);
    }

    @Test
    void shouldThrowWhenElectionClosed_onCreate() {
        CandidateRequest request = new CandidateRequest("John", 10L);
        Election election = new Election();
        election.setId(10L);
        election.setStatus(ElectionStatus.CLOSED);

        when(electionService.findEntityById(10L)).thenReturn(election);

        assertThrows(ElectionClosedException.class, () -> candidateService.create(request));
    }

    @Test
    void shouldThrowWhenChangingElection_onUpdate() {
        CandidateRequest request = new CandidateRequest("John Updated", 20L);

        Election originalElection = new Election();
        originalElection.setId(10L);
        originalElection.setStatus(ElectionStatus.OPEN);

        Candidate existing = new Candidate();
        existing.setId(5L);
        existing.setName("John");
        existing.setElection(originalElection);

        when(candidateRepository.findById(5L)).thenReturn(Optional.of(existing));
        when(electionService.findEntityById(20L)).thenReturn(new Election());

        assertThrows(CandidateElectionChangeNotAllowedException.class, () -> candidateService.update(5L, request));
    }
}

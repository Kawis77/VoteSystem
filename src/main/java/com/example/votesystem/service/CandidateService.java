package com.example.votesystem.service;

import com.example.votesystem.dto.request.CandidateRequest;
import com.example.votesystem.dto.response.CandidateResponse;
import com.example.votesystem.entity.Candidate;
import com.example.votesystem.entity.Election;
import com.example.votesystem.entity.ElectionStatus;
import com.example.votesystem.mapper.CandidateMapper;
import com.example.votesystem.repository.CandidateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final CandidateMapper candidateMapper;
    private final ElectionService electionService;

    @Transactional(readOnly = true)
    public List<CandidateResponse> findAll() {
        return candidateRepository.findAll()
                .stream()
                .map(candidateMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CandidateResponse findById(Long id) {
        Candidate candidate = findEntityById(id);
        return candidateMapper.toResponse(candidate);
    }

    @Transactional
    public CandidateResponse create(CandidateRequest request) {
        Election election = electionService.findEntityById(request.electionId());
        if (election.getStatus() == ElectionStatus.CLOSED) {
            throw new IllegalStateException("Cannot create candidate for closed election: " + request.electionId());
        }

        Candidate candidate = candidateMapper.toEntity(request);
        candidate.setElection(election);
        Candidate saved = candidateRepository.save(candidate);
        return candidateMapper.toResponse(saved);
    }

    @Transactional
    public CandidateResponse update(Long id, CandidateRequest request) {
        Candidate existing = findEntityById(id);
        if (existing.getElection().getStatus() == ElectionStatus.CLOSED) {
            throw new IllegalStateException("Cannot update candidate for closed election: " + existing.getElection().getId());
        }

        electionService.findEntityById(request.electionId());
        if (!existing.getElection().getId().equals(request.electionId())) {
            throw new IllegalStateException("Candidate election cannot be changed after creation");
        }

        candidateMapper.updateEntity(existing, request);
        return candidateMapper.toResponse(existing);
    }

    @Transactional
    public void delete(Long id) {
        Candidate existing = findEntityById(id);
        if (existing.getElection().getStatus() == ElectionStatus.CLOSED) {
            throw new IllegalStateException("Cannot delete candidate for closed election: " + existing.getElection().getId());
        }
        candidateRepository.delete(existing);
    }

    @Transactional(readOnly = true)
    public List<CandidateResponse> findByElectionId(Long electionId) {
        electionService.findEntityById(electionId);
        return candidateRepository.findByElectionId(electionId)
                .stream()
                .map(candidateMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public Candidate findEntityById(Long id) {
        return candidateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Candidate not found: " + id));
    }
}

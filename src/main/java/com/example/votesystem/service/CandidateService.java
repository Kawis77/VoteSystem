package com.example.votesystem.service;

import com.example.votesystem.entity.Candidate;
import com.example.votesystem.repository.CandidateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CandidateService {

    private final CandidateRepository candidateRepository;

    @Transactional(readOnly = true)
    public List<Candidate> findAll() {
        return candidateRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Candidate findById(Long id) {
        return candidateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Candidate not found: " + id));
    }

    @Transactional
    public Candidate create(Candidate candidate) {
        return candidateRepository.save(candidate);
    }

    @Transactional
    public Candidate update(Long id, Candidate candidate) {
        Candidate existing = findById(id);
        existing.setName(candidate.getName());
        existing.setElection(candidate.getElection());
        return candidateRepository.save(existing);
    }

    @Transactional
    public void deleteById(Long id) {
        candidateRepository.deleteById(id);
    }
}

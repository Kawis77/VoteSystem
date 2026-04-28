package com.example.votesystem.service;

import com.example.votesystem.entity.Election;
import com.example.votesystem.repository.ElectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ElectionService {

    private final ElectionRepository electionRepository;

    @Transactional(readOnly = true)
    public List<Election> findAll() {
        return electionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Election findById(Long id) {
        return electionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Election not found: " + id));
    }

    @Transactional
    public Election create(Election election) {
        return electionRepository.save(election);
    }

    @Transactional
    public Election update(Long id, Election election) {
        Election existing = findById(id);
        existing.setName(election.getName());
        existing.setStatus(election.getStatus());
        return electionRepository.save(existing);
    }

    @Transactional
    public void deleteById(Long id) {
        electionRepository.deleteById(id);
    }
}

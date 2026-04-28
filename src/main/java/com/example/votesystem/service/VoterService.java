package com.example.votesystem.service;

import com.example.votesystem.entity.Voter;
import com.example.votesystem.repository.VoterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VoterService {

    private final VoterRepository voterRepository;

    @Transactional(readOnly = true)
    public List<Voter> findAll() {
        return voterRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Voter findById(Long id) {
        return voterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Voter not found: " + id));
    }

    @Transactional
    public Voter create(Voter voter) {
        return voterRepository.save(voter);
    }

    @Transactional
    public Voter update(Long id, Voter voter) {
        Voter existing = findById(id);
        existing.setName(voter.getName());
        existing.setBlocked(voter.isBlocked());
        return voterRepository.save(existing);
    }

    @Transactional
    public void deleteById(Long id) {
        voterRepository.deleteById(id);
    }
}

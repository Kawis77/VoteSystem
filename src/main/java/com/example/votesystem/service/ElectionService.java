package com.example.votesystem.service;

import com.example.votesystem.dto.request.ElectionRequest;
import com.example.votesystem.dto.response.ElectionResponse;
import com.example.votesystem.entity.Election;
import com.example.votesystem.entity.ElectionStatus;
import com.example.votesystem.mapper.ElectionMapper;
import com.example.votesystem.repository.ElectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ElectionService {

    private final ElectionRepository electionRepository;
    private final ElectionMapper electionMapper;

    @Transactional(readOnly = true)
    public List<ElectionResponse> findAll() {
        return electionRepository.findAll()
                .stream()
                .map(electionMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ElectionResponse findById(Long id) {
        Election election = findEntityById(id);
        return electionMapper.toResponse(election);
    }

    @Transactional
    public ElectionResponse create(ElectionRequest request) {
        Election election = electionMapper.toEntity(request);
        if (election.getStatus() == null) {
            election.setStatus(ElectionStatus.OPEN);
        }
        Election saved = electionRepository.save(election);
        return electionMapper.toResponse(saved);
    }

    @Transactional
    public ElectionResponse update(Long id, ElectionRequest request) {
        Election existing = findEntityById(id);
        if (existing.getStatus() == ElectionStatus.CLOSED) {
            throw new IllegalStateException("Cannot update closed election: " + id);
        }
        electionMapper.updateEntity(existing, request);
        if (existing.getStatus() == null) {
            existing.setStatus(ElectionStatus.OPEN);
        }
        return electionMapper.toResponse(existing);
    }

    @Transactional
    public void delete(Long id) {
        Election existing = findEntityById(id);
        electionRepository.delete(existing);
    }

    @Transactional
    public ElectionResponse changeStatus(Long id, ElectionStatus status) {
        Election existing = findEntityById(id);
        existing.setStatus(status);
        return electionMapper.toResponse(existing);
    }

    @Transactional(readOnly = true)
    public Election findEntityById(Long id) {
        return electionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Election not found: " + id));
    }
}

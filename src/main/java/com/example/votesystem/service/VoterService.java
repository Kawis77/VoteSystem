package com.example.votesystem.service;

import com.example.votesystem.dto.request.VoterRequest;
import com.example.votesystem.dto.response.VoterResponse;
import com.example.votesystem.entity.Voter;
import com.example.votesystem.exception.NotFoundException;
import com.example.votesystem.mapper.VoterMapper;
import com.example.votesystem.repository.VoterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VoterService {

    private final VoterRepository voterRepository;
    private final VoterMapper voterMapper;

    @Transactional(readOnly = true)
    public List<VoterResponse> findAll() {
        return voterRepository.findAll()
                .stream()
                .map(voterMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public VoterResponse findById(Long id) {
        Voter voter = findEntityById(id);
        return voterMapper.toResponse(voter);
    }

    @Transactional
    public VoterResponse create(VoterRequest request) {
        Voter voter = voterMapper.toEntity(request);
        if (request.blocked() == null) {
            voter.setBlocked(false);
        }
        Voter saved = voterRepository.save(voter);
        return voterMapper.toResponse(saved);
    }

    @Transactional
    public VoterResponse update(Long id, VoterRequest request) {
        Voter existing = findEntityById(id);
        voterMapper.updateEntity(existing, request);
        return voterMapper.toResponse(existing);
    }

    @Transactional
    public void delete(Long id) {
        Voter voter = findEntityById(id);
        voterRepository.delete(voter);
    }

    @Transactional
    public VoterResponse block(Long id) {
        Voter voter = findEntityById(id);
        voter.setBlocked(true);
        return voterMapper.toResponse(voter);
    }

    @Transactional
    public VoterResponse unblock(Long id) {
        Voter voter = findEntityById(id);
        voter.setBlocked(false);
        return voterMapper.toResponse(voter);
    }

    @Transactional(readOnly = true)
    public Voter findEntityById(Long id) {
        return voterRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Voter not found: " + id));
    }
}

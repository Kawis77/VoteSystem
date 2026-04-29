package com.example.votesystem.service;

import com.example.votesystem.dto.request.ElectionRequest;
import com.example.votesystem.dto.response.ElectionResponse;
import com.example.votesystem.entity.Election;
import com.example.votesystem.entity.ElectionStatus;
import com.example.votesystem.exception.ElectionClosedException;
import com.example.votesystem.mapper.ElectionMapper;
import com.example.votesystem.repository.ElectionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ElectionServiceTest {

    @Mock
    private ElectionRepository electionRepository;
    @Mock
    private ElectionMapper electionMapper;

    @InjectMocks
    private ElectionService electionService;

    @Test
    void shouldCreateElectionWithDefaultStatus_whenStatusIsNull() {
        ElectionRequest request = new ElectionRequest("Parliament 2026", null);
        Election election = new Election();
        election.setName("Parliament 2026");
        election.setStatus(null);

        Election saved = new Election();
        saved.setId(1L);
        saved.setName("Parliament 2026");
        saved.setStatus(ElectionStatus.OPEN);

        ElectionResponse response = new ElectionResponse(1L, "Parliament 2026", ElectionStatus.OPEN);

        when(electionMapper.toEntity(request)).thenReturn(election);
        when(electionRepository.save(election)).thenAnswer(invocation -> {
            Election arg = invocation.getArgument(0);
            saved.setStatus(arg.getStatus());
            return saved;
        });
        when(electionMapper.toResponse(saved)).thenReturn(response);

        ElectionResponse result = electionService.create(request);

        assertEquals(ElectionStatus.OPEN, result.status());
    }

    @Test
    void shouldThrowWhenUpdatingClosedElection() {
        ElectionRequest request = new ElectionRequest("Updated Name", ElectionStatus.OPEN);
        Election existing = new Election();
        existing.setId(10L);
        existing.setStatus(ElectionStatus.CLOSED);

        when(electionRepository.findById(10L)).thenReturn(Optional.of(existing));

        assertThrows(ElectionClosedException.class, () -> electionService.update(10L, request));
    }
}

package com.example.votesystem.service;

import com.example.votesystem.dto.request.VoterRequest;
import com.example.votesystem.dto.response.VoterResponse;
import com.example.votesystem.entity.Voter;
import com.example.votesystem.mapper.VoterMapper;
import com.example.votesystem.repository.VoterRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VoterServiceTest {

    @Mock
    private VoterRepository voterRepository;
    @Mock
    private VoterMapper voterMapper;

    @InjectMocks
    private VoterService voterService;

    @Test
    void shouldCreateVoterWithDefaultUnblocked_whenBlockedIsNull() {
        VoterRequest request = new VoterRequest("Alice", null);
        Voter voter = new Voter();
        voter.setName("Alice");

        Voter saved = new Voter();
        saved.setId(1L);
        saved.setName("Alice");
        saved.setBlocked(false);

        VoterResponse response = new VoterResponse(1L, "Alice", false);

        when(voterMapper.toEntity(request)).thenReturn(voter);
        when(voterRepository.save(voter)).thenAnswer(invocation -> {
            Voter arg = invocation.getArgument(0);
            saved.setBlocked(arg.isBlocked());
            return saved;
        });
        when(voterMapper.toResponse(saved)).thenReturn(response);

        VoterResponse result = voterService.create(request);

        assertFalse(result.blocked());
    }

    @Test
    void shouldBlockVoter() {
        Voter voter = new Voter();
        voter.setId(1L);
        voter.setBlocked(false);
        VoterResponse response = new VoterResponse(1L, "Alice", true);

        when(voterRepository.findById(1L)).thenReturn(Optional.of(voter));
        when(voterMapper.toResponse(voter)).thenReturn(response);

        VoterResponse result = voterService.block(1L);

        assertTrue(result.blocked());
    }

    @Test
    void shouldUnblockVoter() {
        Voter voter = new Voter();
        voter.setId(1L);
        voter.setBlocked(true);
        VoterResponse response = new VoterResponse(1L, "Alice", false);

        when(voterRepository.findById(1L)).thenReturn(Optional.of(voter));
        when(voterMapper.toResponse(voter)).thenReturn(response);

        VoterResponse result = voterService.unblock(1L);

        assertFalse(result.blocked());
    }
}

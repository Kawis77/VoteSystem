package com.example.votesystem.controller;

import com.example.votesystem.dto.request.VoterRequest;
import com.example.votesystem.dto.response.VoteByVoterResponse;
import com.example.votesystem.dto.response.VoterResponse;
import com.example.votesystem.service.VoteService;
import com.example.votesystem.service.VoterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/voters")
@RequiredArgsConstructor
public class VoterController {

    private final VoterService voterService;
    private final VoteService voteService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VoterResponse create(@Valid @RequestBody VoterRequest request) {
        return voterService.create(request);
    }

    @GetMapping
    public List<VoterResponse> findAll() {
        return voterService.findAll();
    }

    @GetMapping("/{id}")
    public VoterResponse findById(@PathVariable Long id) {
        return voterService.findById(id);
    }

    @GetMapping("/{id}/votes")
    public List<VoteByVoterResponse> findVotesByVoterId(@PathVariable Long id) {
        return voteService.findByVoterId(id);
    }

    @PutMapping("/{id}")
    public VoterResponse update(@PathVariable Long id, @Valid @RequestBody VoterRequest request) {
        return voterService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        voterService.delete(id);
    }

    @PatchMapping("/{id}/block")
    public VoterResponse block(@PathVariable Long id) {
        return voterService.block(id);
    }

    @PatchMapping("/{id}/unblock")
    public VoterResponse unblock(@PathVariable Long id) {
        return voterService.unblock(id);
    }
}

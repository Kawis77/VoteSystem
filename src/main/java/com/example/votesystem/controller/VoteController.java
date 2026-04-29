package com.example.votesystem.controller;

import com.example.votesystem.dto.request.VoteRequest;
import com.example.votesystem.dto.response.VoteResponse;
import com.example.votesystem.service.VoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/votes")
@RequiredArgsConstructor
public class VoteController {

    private final VoteService voteService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VoteResponse castVote(@Valid @RequestBody VoteRequest request) {
        return voteService.castVote(request);
    }

    @GetMapping
    public List<VoteResponse> findAll() {
        return voteService.findAll();
    }

    @GetMapping("/{id}")
    public VoteResponse findById(@PathVariable Long id) {
        return voteService.findById(id);
    }
}

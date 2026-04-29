package com.example.votesystem.controller;

import com.example.votesystem.dto.request.ElectionRequest;
import com.example.votesystem.dto.response.ElectionResponse;
import com.example.votesystem.entity.ElectionStatus;
import com.example.votesystem.service.ElectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/elections")
@RequiredArgsConstructor
public class ElectionController {

    private final ElectionService electionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ElectionResponse create(@Valid @RequestBody ElectionRequest request) {
        return electionService.create(request);
    }

    @GetMapping
    public List<ElectionResponse> findAll() {
        return electionService.findAll();
    }

    @GetMapping("/{id}")
    public ElectionResponse findById(@PathVariable Long id) {
        return electionService.findById(id);
    }

    @PutMapping("/{id}")
    public ElectionResponse update(@PathVariable Long id, @Valid @RequestBody ElectionRequest request) {
        return electionService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        electionService.delete(id);
    }

    @PatchMapping("/{id}/status")
    public ElectionResponse changeStatus(@PathVariable Long id, @RequestParam ElectionStatus status) {
        return electionService.changeStatus(id, status);
    }
}

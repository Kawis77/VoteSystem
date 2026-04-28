package com.example.votesystem.dto.response;

public record CandidateResponse(
        Long id,
        String name,
        Long electionId
) {
}

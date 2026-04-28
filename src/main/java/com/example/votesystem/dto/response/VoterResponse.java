package com.example.votesystem.dto.response;

public record VoterResponse(
        Long id,
        String name,
        boolean blocked
) {
}

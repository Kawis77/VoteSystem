package com.example.votesystem.dto.response;

public record VoteResponse(
        Long id,
        Long voterId,
        Long electionId,
        Long candidateId
) {
}

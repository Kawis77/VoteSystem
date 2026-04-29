package com.example.votesystem.dto.response;

public record VoteByVoterResponse(
        Long electionId,
        String electionName,
        Long candidateId,
        String candidateName
) {
}

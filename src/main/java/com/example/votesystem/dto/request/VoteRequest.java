package com.example.votesystem.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record VoteRequest(
        @NotNull
        @Positive
        Long voterId,

        @NotNull
        @Positive
        Long electionId,

        @NotNull
        @Positive
        Long candidateId
) {
}

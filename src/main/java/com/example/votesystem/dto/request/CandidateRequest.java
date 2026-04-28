package com.example.votesystem.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CandidateRequest(
        @NotBlank
        @Size(max = 120)
        String name,

        @NotNull
        @Positive
        Long electionId
) {
}

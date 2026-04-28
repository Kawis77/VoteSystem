package com.example.votesystem.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record VoterRequest(
        @NotBlank
        @Size(max = 120)
        String name,
        Boolean blocked
) {
}

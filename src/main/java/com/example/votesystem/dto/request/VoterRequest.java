package com.example.votesystem.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record VoterRequest(
        @NotBlank
        @Size(max = 120)
        String name,
        @Schema(description = "Voter blocked flag", defaultValue = "false", example = "false")
        Boolean blocked
) {
}

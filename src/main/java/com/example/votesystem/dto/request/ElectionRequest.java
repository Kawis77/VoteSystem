package com.example.votesystem.dto.request;

import com.example.votesystem.entity.ElectionStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ElectionRequest(
        @NotBlank
        @Size(max = 255)
        String name,

        @NotNull
        ElectionStatus status
) {
}

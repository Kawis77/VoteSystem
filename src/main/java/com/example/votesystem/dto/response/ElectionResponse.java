package com.example.votesystem.dto.response;

import com.example.votesystem.entity.ElectionStatus;

public record ElectionResponse(
        Long id,
        String name,
        ElectionStatus status
) {
}

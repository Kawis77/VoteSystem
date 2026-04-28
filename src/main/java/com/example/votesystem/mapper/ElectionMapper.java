package com.example.votesystem.mapper;

import com.example.votesystem.dto.request.ElectionRequest;
import com.example.votesystem.dto.response.ElectionResponse;
import com.example.votesystem.entity.Election;
import org.springframework.stereotype.Component;

@Component
public class ElectionMapper {

    public Election toEntity(ElectionRequest request) {
        Election election = new Election();
        election.setName(request.name());
        election.setStatus(request.status());
        return election;
    }

    public void updateEntity(Election election, ElectionRequest request) {
        election.setName(request.name());
        election.setStatus(request.status());
    }

    public ElectionResponse toResponse(Election election) {
        return new ElectionResponse(
                election.getId(),
                election.getName(),
                election.getStatus()
        );
    }
}

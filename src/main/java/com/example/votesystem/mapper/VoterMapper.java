package com.example.votesystem.mapper;

import com.example.votesystem.dto.request.VoterRequest;
import com.example.votesystem.dto.response.VoterResponse;
import com.example.votesystem.entity.Voter;
import org.springframework.stereotype.Component;

@Component
public class VoterMapper {

    public Voter toEntity(VoterRequest request) {
        Voter voter = new Voter();
        voter.setName(request.name());
        voter.setBlocked(Boolean.TRUE.equals(request.blocked()));
        return voter;
    }

    public void updateEntity(Voter voter, VoterRequest request) {
        voter.setName(request.name());
        if (request.blocked() != null) {
            voter.setBlocked(request.blocked());
        }
    }

    public VoterResponse toResponse(Voter voter) {
        return new VoterResponse(
                voter.getId(),
                voter.getName(),
                voter.isBlocked()
        );
    }
}

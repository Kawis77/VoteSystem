package com.example.votesystem.mapper;

import com.example.votesystem.dto.request.CandidateRequest;
import com.example.votesystem.dto.response.CandidateResponse;
import com.example.votesystem.entity.Candidate;
import org.springframework.stereotype.Component;

@Component
public class CandidateMapper {

    public Candidate toEntity(CandidateRequest request) {
        Candidate candidate = new Candidate();
        candidate.setName(request.name());
        return candidate;
    }

    public void updateEntity(Candidate candidate, CandidateRequest request) {
        candidate.setName(request.name());
    }

    public CandidateResponse toResponse(Candidate candidate) {
        return new CandidateResponse(
                candidate.getId(),
                candidate.getName(),
                candidate.getElection().getId()
        );
    }
}

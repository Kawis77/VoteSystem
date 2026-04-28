package com.example.votesystem.mapper;

import com.example.votesystem.dto.request.CandidateRequest;
import com.example.votesystem.dto.response.CandidateResponse;
import com.example.votesystem.entity.Candidate;
import com.example.votesystem.entity.Election;
import org.springframework.stereotype.Component;

@Component
public class CandidateMapper {

    public Candidate toEntity(CandidateRequest request, Election election) {
        Candidate candidate = new Candidate();
        candidate.setName(request.name());
        candidate.setElection(election);
        return candidate;
    }

    public void updateEntity(Candidate candidate, CandidateRequest request, Election election) {
        candidate.setName(request.name());
        candidate.setElection(election);
    }

    public CandidateResponse toResponse(Candidate candidate) {
        return new CandidateResponse(
                candidate.getId(),
                candidate.getName(),
                candidate.getElection().getId()
        );
    }
}

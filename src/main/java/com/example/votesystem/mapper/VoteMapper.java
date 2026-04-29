package com.example.votesystem.mapper;

import com.example.votesystem.dto.request.VoteRequest;
import com.example.votesystem.dto.response.VoteByVoterResponse;
import com.example.votesystem.dto.response.VoteResponse;
import com.example.votesystem.entity.Candidate;
import com.example.votesystem.entity.Election;
import com.example.votesystem.entity.Vote;
import org.springframework.stereotype.Component;

@Component
public class VoteMapper {

    public Vote toEntity(VoteRequest request, Election election, Candidate candidate) {
        Vote vote = new Vote();
        vote.setVoterId(request.voterId());
        vote.setElection(election);
        vote.setCandidate(candidate);
        return vote;
    }

    public void updateEntity(Vote vote, VoteRequest request, Election election, Candidate candidate) {
        vote.setVoterId(request.voterId());
        vote.setElection(election);
        vote.setCandidate(candidate);
    }

    public VoteResponse toResponse(Vote vote) {
        return new VoteResponse(
                vote.getId(),
                vote.getVoterId(),
                vote.getElection().getId(),
                vote.getCandidate().getId()
        );
    }

    public VoteByVoterResponse toVoteByVoterResponse(Vote vote) {
        return new VoteByVoterResponse(
                vote.getElection().getId(),
                vote.getElection().getName(),
                vote.getCandidate().getId(),
                vote.getCandidate().getName()
        );
    }
}

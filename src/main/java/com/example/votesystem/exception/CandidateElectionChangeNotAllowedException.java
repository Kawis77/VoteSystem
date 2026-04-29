package com.example.votesystem.exception;

public class CandidateElectionChangeNotAllowedException extends BusinessException {

    public CandidateElectionChangeNotAllowedException(String message) {
        super(message);
    }

    public CandidateElectionChangeNotAllowedException(String message, Throwable cause) {
        super(message, cause);
    }
}

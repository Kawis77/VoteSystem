package com.example.votesystem.exception;

public class VoterBlockedException extends BusinessException {

    public VoterBlockedException(String message) {
        super(message);
    }

    public VoterBlockedException(String message, Throwable cause) {
        super(message, cause);
    }
}

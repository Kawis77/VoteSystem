package com.example.votesystem.exception;

public class CandidateMismatchException extends BusinessException {

    public CandidateMismatchException(String message) {
        super(message);
    }

    public CandidateMismatchException(String message, Throwable cause) {
        super(message, cause);
    }
}

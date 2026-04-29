package com.example.votesystem.exception;

public class ElectionClosedException extends BusinessException {

    public ElectionClosedException(String message) {
        super(message);
    }

    public ElectionClosedException(String message, Throwable cause) {
        super(message, cause);
    }
}

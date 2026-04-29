package com.example.votesystem.exception;

public class DuplicateVoteException extends BusinessException {

    public DuplicateVoteException(String message) {
        super(message);
    }

    public DuplicateVoteException(String message, Throwable cause) {
        super(message, cause);
    }
}

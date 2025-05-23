package com.UAIC.ISMA.exception;

public class VirtualAccessAlreadyExistsException extends RuntimeException {
    public VirtualAccessAlreadyExistsException(Long requestId) {
        super("Virtual access already exists for access request ID: " + requestId);
    }
}


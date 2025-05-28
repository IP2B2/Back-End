package com.UAIC.ISMA.exception;

public class VirtualAccessAlreadyExistsException extends ConflictException {
    public VirtualAccessAlreadyExistsException(Long requestId) {
        super("Virtual access already exists for access request ID: " + requestId);
    }
}


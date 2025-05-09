package com.UAIC.ISMA.exception;

public class VirtualAccessNotFoundException extends EntityNotFoundException {
    public VirtualAccessNotFoundException(Long id) {
        super("Virtual access not found with id " + id);
    }
}

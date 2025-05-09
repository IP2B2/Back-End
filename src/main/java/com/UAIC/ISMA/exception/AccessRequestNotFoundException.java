package com.UAIC.ISMA.exception;

public class AccessRequestNotFoundException extends EntityNotFoundException {
    public AccessRequestNotFoundException(Long id) {
        super("Access request not found with ID: " + id);
    }
}
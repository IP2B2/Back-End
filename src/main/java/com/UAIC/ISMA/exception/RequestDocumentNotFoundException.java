package com.UAIC.ISMA.exception;

public class RequestDocumentNotFoundException extends EntityNotFoundException {
    public RequestDocumentNotFoundException(Long id) {
        super("RequestDocument not found with ID: " + id);
    }
}

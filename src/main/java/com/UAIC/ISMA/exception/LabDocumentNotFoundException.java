package com.UAIC.ISMA.exception;

public class LabDocumentNotFoundException extends RuntimeException {
    public LabDocumentNotFoundException(Long id) {
        super("LabDocument not found with ID: " + id);
    }
}



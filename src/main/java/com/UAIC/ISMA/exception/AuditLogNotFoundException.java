package com.UAIC.ISMA.exception;

public class AuditLogNotFoundException extends RuntimeException {
    public AuditLogNotFoundException(Long id) {
        super("AuditLog not found with ID: " + id);
    }
}

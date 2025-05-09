package com.UAIC.ISMA.exception;

public class RequestApprovalNotFoundException extends EntityNotFoundException {
    public RequestApprovalNotFoundException(Long id) {
        super("Request approval not found with ID: " + id);
    }
}

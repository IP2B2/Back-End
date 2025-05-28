package com.UAIC.ISMA.exception;


public class MissingFieldException extends RuntimeException {
    public MissingFieldException(String fieldName) {
        super(fieldName + " is required.");
    }
}

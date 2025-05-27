package com.UAIC.ISMA.exception;

public class MissingAccessRequestIdException extends RuntimeException {
    public MissingAccessRequestIdException() {
        super("accessRequestId can not be null");
    }
}

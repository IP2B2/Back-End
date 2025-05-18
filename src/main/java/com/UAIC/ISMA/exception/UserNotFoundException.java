package com.UAIC.ISMA.exception;


public class UserNotFoundException extends EntityNotFoundException {

    public UserNotFoundException(Long id) {
        super("User not found with ID: " + id);
    }
    public UserNotFoundException(String message) {
        super(message);
    }
}

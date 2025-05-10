package com.UAIC.ISMA.exception;

public class LaboratoryNotFoundException extends EntityNotFoundException {
    public LaboratoryNotFoundException(Long id) {
        super("Laboratory not found with id " + id);
    }
}

package com.UAIC.ISMA.exception;

public class EquipmentNotFoundException extends EntityNotFoundException {
    public EquipmentNotFoundException(Long id) {
        super("Equipment not found with ID: " + id);
    }
}

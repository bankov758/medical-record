package org.example.medicalrecord.exceptions;

public class EntityNotFoundException extends RuntimeException {

    public <T> EntityNotFoundException(Class<T> type, String attribute, Object value) {
        super(String.format("%s with %s %s was not found!", type.getSimpleName(), attribute, value));
    }

}

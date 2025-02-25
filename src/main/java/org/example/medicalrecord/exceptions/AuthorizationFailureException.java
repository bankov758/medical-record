package org.example.medicalrecord.exceptions;

public class AuthorizationFailureException extends RuntimeException {

    public AuthorizationFailureException(String message) {
        super(message);
    }

}

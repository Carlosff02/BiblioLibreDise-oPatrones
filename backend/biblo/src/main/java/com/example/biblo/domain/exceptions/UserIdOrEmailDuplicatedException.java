package com.example.biblo.domain.exceptions;

public class UserIdOrEmailDuplicatedException extends RuntimeException {
    public UserIdOrEmailDuplicatedException(String userIdOrEmail) {
        super("El usuario "+userIdOrEmail+" ya existe");
    }
}

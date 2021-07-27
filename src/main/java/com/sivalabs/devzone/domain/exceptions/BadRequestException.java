package com.sivalabs.devzone.domain.exceptions;

public class BadRequestException extends DevZoneException {
    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(Throwable cause) {
        super(cause);
    }
}

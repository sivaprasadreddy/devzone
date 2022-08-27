package com.sivalabs.devzone.common.exceptions;

public class BadRequestException extends DevZoneException {
    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(Throwable cause) {
        super(cause);
    }
}

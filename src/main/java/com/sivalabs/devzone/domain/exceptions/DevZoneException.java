package com.sivalabs.devzone.domain.exceptions;

public class DevZoneException extends RuntimeException {
    public DevZoneException(String message) {
        super(message);
    }

    public DevZoneException(Throwable cause) {
        super(cause);
    }
}

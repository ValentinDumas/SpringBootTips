package com.xeon.SpringBootTips.reservation.exceptions;

public class ReservationReferenceValidationException extends RuntimeException {

    public ReservationReferenceValidationException(String message) {
        super(message);
    }

    public ReservationReferenceValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}

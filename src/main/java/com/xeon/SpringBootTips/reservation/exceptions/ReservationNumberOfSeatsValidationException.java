package com.xeon.SpringBootTips.reservation.exceptions;

public class ReservationNumberOfSeatsValidationException extends RuntimeException {

    public ReservationNumberOfSeatsValidationException(String message) {
        super(message);
    }

    public ReservationNumberOfSeatsValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}

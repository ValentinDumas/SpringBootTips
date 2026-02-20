package com.xeon.SpringBootTips.reservation.exceptions;

public class ReservationUserInfoValidationException extends RuntimeException {

    public ReservationUserInfoValidationException(String message) {
        super(message);
    }

    public ReservationUserInfoValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}

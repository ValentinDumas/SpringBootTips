package com.xeon.SpringBootTips.reservation.exceptions;

public class ReservationFormulaValidationException extends RuntimeException {

    public ReservationFormulaValidationException(String message) {
        super(message);
    }

    public ReservationFormulaValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}

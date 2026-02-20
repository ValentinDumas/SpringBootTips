package com.xeon.SpringBootTips.reservation.validators;

import com.xeon.SpringBootTips.reservation.model.ReservationOrder;

public interface FlightReservationValidator {

    void validate(ReservationOrder reservationOrder);
}

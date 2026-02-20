package com.xeon.SpringBootTips.reservation.validators;

import com.xeon.SpringBootTips.reservation.exceptions.ReservationNumberOfSeatsValidationException;
import com.xeon.SpringBootTips.reservation.model.ReservationOrder;
import org.springframework.stereotype.Component;

@Component
public class ReservationNumberOfSeatsValidator implements FlightReservationValidator {

    @Override
    public void validate(ReservationOrder reservationOrder) {
        Integer numberOfSeats = reservationOrder.getNumberOfSeats();

        if (numberOfSeats <= 0) {
            throw new ReservationNumberOfSeatsValidationException("Reservation should have at least one seat. Current is " + numberOfSeats);
        }
    }
}

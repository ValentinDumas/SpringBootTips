package com.xeon.SpringBootTips.reservation;

import com.xeon.SpringBootTips.reservation.model.ReservationOrder;
import com.xeon.SpringBootTips.reservation.validators.FlightReservationValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlightReservationService {

    private final List<FlightReservationValidator> flightReservationValidators;

    public FlightReservationService(List<FlightReservationValidator> flightReservationValidators) {
        this.flightReservationValidators = flightReservationValidators;
    }

    public void processReservation(ReservationOrder reservationOrder) {
        flightReservationValidators.forEach(validator -> validator.validate(reservationOrder));
    }
}

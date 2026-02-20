package com.xeon.SpringBootTips.reservation.validators;

import com.xeon.SpringBootTips.reservation.exceptions.ReservationFormulaValidationException;
import com.xeon.SpringBootTips.reservation.model.ReservationOrder;
import com.xeon.SpringBootTips.reservation.model.ReservationPrivilege;
import com.xeon.SpringBootTips.reservation.model.ReservationType;
import org.springframework.stereotype.Component;

@Component
public class ReservationFormulaValidator implements FlightReservationValidator {

    @Override
    public void validate(ReservationOrder reservationOrder) {
        ReservationType type = reservationOrder.getReservationType();
        ReservationPrivilege privilege = reservationOrder.getReservationPrivilege();

        // Note: I NEVER negates business method but it was way clearer for this case
        if (reservationFormulaDoesNotMatchPolicy(privilege, type)) {
            throw new ReservationFormulaValidationException(
                    "Reservation is incompatible with the chosen formula: " + privilege + " | " + type);
        }
    }

    private boolean reservationFormulaDoesNotMatchPolicy(ReservationPrivilege privilege, ReservationType type) {
        return ReservationPrivilege.NONE.equals(privilege) && ReservationType.LAST_MINUTE.equals(type);
    }
}

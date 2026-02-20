package com.xeon.SpringBootTips.reservation.validators;

import com.xeon.SpringBootTips.reservation.exceptions.ReservationUserInfoValidationException;
import com.xeon.SpringBootTips.reservation.model.ReservationOrder;
import com.xeon.SpringBootTips.utils.StringHelper;
import org.springframework.stereotype.Component;

@Component
public class ReservationUserInfoValidator implements FlightReservationValidator {

    @Override
    public void validate(ReservationOrder reservationOrder) {
        String firstName = reservationOrder.getFirstName();

        if (StringHelper.isEmptyOrBlank(firstName)) {
            throw new ReservationUserInfoValidationException("User first name is blank or empty");
        }

        if (!StringHelper.isAlpha(firstName)) {
            throw new ReservationUserInfoValidationException("User first name is not a sentence but a number: " + firstName);
        }
    }
}

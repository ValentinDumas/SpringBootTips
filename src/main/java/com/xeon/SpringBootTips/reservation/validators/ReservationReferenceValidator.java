package com.xeon.SpringBootTips.reservation.validators;

import com.xeon.SpringBootTips.reservation.exceptions.ReservationReferenceValidationException;
import com.xeon.SpringBootTips.reservation.model.ReservationOrder;
import com.xeon.SpringBootTips.utils.StringHelper;
import org.springframework.stereotype.Component;

@Component
public class ReservationReferenceValidator implements FlightReservationValidator {

    public static final int UUID_LENGTH_REFERENCE = 36;

    @Override
    public void validate(ReservationOrder reservationOrder) {
        String uuid = reservationOrder.getUuid();

        if (StringHelper.isEmptyOrBlank(uuid)) {
            throw new ReservationReferenceValidationException("UUID is blank or empty");
        }

        if (!isSizeCompliant(uuid)) {
            throw new ReservationReferenceValidationException("UUID length is not equal to " + UUID_LENGTH_REFERENCE + ". Current is " + uuid.length());
        }
    }

    private boolean isSizeCompliant(String uuid) {
        return uuid.length() == UUID_LENGTH_REFERENCE;
    }
}

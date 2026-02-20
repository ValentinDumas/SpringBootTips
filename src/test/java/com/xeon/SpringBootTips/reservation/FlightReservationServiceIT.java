package com.xeon.SpringBootTips.reservation;

import com.xeon.SpringBootTips.reservation.exceptions.ReservationFormulaValidationException;
import com.xeon.SpringBootTips.reservation.exceptions.ReservationNumberOfSeatsValidationException;
import com.xeon.SpringBootTips.reservation.exceptions.ReservationReferenceValidationException;
import com.xeon.SpringBootTips.reservation.exceptions.ReservationUserInfoValidationException;
import com.xeon.SpringBootTips.reservation.model.ReservationOrder;
import com.xeon.SpringBootTips.reservation.model.ReservationPrivilege;
import com.xeon.SpringBootTips.reservation.model.ReservationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@SpringBootTest
public class FlightReservationServiceIT {

    @Autowired
    private FlightReservationService flightReservationService;

    private ReservationOrder reservationOrder;

    private static Stream<Arguments> getValidReservationTypeVersusPrivilegeCouples() {
        return Stream.of(
                arguments(ReservationPrivilege.NONE, ReservationType.REGULAR),
                arguments(ReservationPrivilege.BUSINESS, ReservationType.REGULAR),
                arguments(ReservationPrivilege.BUSINESS, ReservationType.LAST_MINUTE)
        );
    }

    @BeforeEach
    void beforeEach() {
        reservationOrder = new ReservationOrder()
                .setUuid(String.valueOf(UUID.randomUUID()))
                .setFirstName("You")
                .setLastName("The one")
                .setReservationPrivilege(ReservationPrivilege.NONE)
                .setReservationType(ReservationType.REGULAR)
                .setNumberOfSeats(1);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "-1", "0", "aaaa", "aaaaaaaaa_aaaaaaaaa_aaaaaaaaa_aaaaaaa", "", "    ", "                                    "
    })
    @DisplayName("should throw ReservationReferenceValidationException when reservation order has bullshit uuid")
    void shouldThrowReservationReferenceValidationExceptionWhenReservationOrderHasBullshitUuid(String invalidUUID) {
        ReservationOrder invalidReservationUUID = reservationOrder.setUuid(invalidUUID);

        assertThatThrownBy(() -> flightReservationService.processReservation(invalidReservationUUID))
                .isExactlyInstanceOf(ReservationReferenceValidationException.class);
//                .hasMessageContaining("...");      ...and so on
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "", "0", " - 1.a", "1a"})
    @DisplayName("should throw ReservationUserInfoValidationException when reservation order has non-alpha firstname")
    void shouldThrowReservationUserInfoValidationExceptionWhenReservationOrderHasHasNonAlphaFirstname(String invalidFirstName) {
        ReservationOrder invalidReservationFirstname = reservationOrder.setFirstName(invalidFirstName);

        assertThatThrownBy(() -> flightReservationService.processReservation(invalidReservationFirstname))
                .isExactlyInstanceOf(ReservationUserInfoValidationException.class);
    }

    @Test
    @DisplayName("should throw ReservationFormulaValidationException when reservation type and privileges are not matching policies")
    void shouldThrowReservationFormulaValidationExceptionWhenReservationTypeAndPrivilegesAreNotMatchingPolicies() {
        ReservationOrder invalidReservationFormula = reservationOrder
                .setReservationPrivilege(ReservationPrivilege.NONE)
                .setReservationType(ReservationType.LAST_MINUTE);

        assertThatThrownBy(() -> flightReservationService.processReservation(invalidReservationFormula))
                .isExactlyInstanceOf(ReservationFormulaValidationException.class);
    }

    @ParameterizedTest
    @MethodSource("getValidReservationTypeVersusPrivilegeCouples")
    @DisplayName("should not throw ReservationFormulaValidationException when reservation type and privileges are matching policies")
    void shouldNotThrowReservationFormulaValidationExceptionWhenReservationTypeAndPrivilegesAreMatchingPolicies(
            ReservationPrivilege reservationPrivilege, ReservationType reservationType) {
        ReservationOrder validReservationFormula = reservationOrder
                .setReservationPrivilege(reservationPrivilege)
                .setReservationType(reservationType);

        assertThatCode(() -> flightReservationService.processReservation(validReservationFormula)).doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    @DisplayName("should throw ReservationInvalidNumberOfSeatsException when number of seats is negative or zero")
    void shouldThrowReservationInvalidNumberOfSeatsExceptionWhenNumberOfSeatsIsNegativeOrZero(Integer invalidNumberOfSeats) {
        ReservationOrder invalidReservationOnNumberOfSeats = reservationOrder.setNumberOfSeats(invalidNumberOfSeats);

        assertThatThrownBy(() -> flightReservationService.processReservation(invalidReservationOnNumberOfSeats))
                .isExactlyInstanceOf(ReservationNumberOfSeatsValidationException.class);
    }

    @Test
    @DisplayName("should not throw any exception nor shout like an angry mother when reservation is valid")
    void shouldNotThrowAnyExceptionNorShoutLikeAnAngryMotherWhenReservationIsValid() {
        assertThatCode(() -> flightReservationService.processReservation(reservationOrder)).doesNotThrowAnyException();
    }
}

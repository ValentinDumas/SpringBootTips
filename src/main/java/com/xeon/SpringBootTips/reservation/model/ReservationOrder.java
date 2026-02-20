package com.xeon.SpringBootTips.reservation.model;

public class ReservationOrder {

    String uuid;

    String firstName;

    String lastName;

    ReservationType reservationType;

    ReservationPrivilege reservationPrivilege;

    Integer numberOfSeats;

    public ReservationOrder() {
    }

    public String getUuid() {
        return uuid;
    }

    public ReservationOrder setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public ReservationOrder setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public ReservationOrder setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public ReservationType getReservationType() {
        return reservationType;
    }

    public ReservationOrder setReservationType(ReservationType reservationType) {
        this.reservationType = reservationType;
        return this;
    }

    public ReservationPrivilege getReservationPrivilege() {
        return reservationPrivilege;
    }

    public ReservationOrder setReservationPrivilege(ReservationPrivilege reservationPrivilege) {
        this.reservationPrivilege = reservationPrivilege;
        return this;
    }

    public Integer getNumberOfSeats() {
        return numberOfSeats;
    }

    public ReservationOrder setNumberOfSeats(Integer numPeople) {
        this.numberOfSeats = numPeople;
        return this;
    }
}

package com.example.aviasale.domain.entity;

import com.example.aviasale.domain.custom_types.Interval;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;


@Entity
@Table(name = "flights")
@TypeDef(name = "interval", typeClass = Interval.class)
public class Flights {

    @Id
    @Column(name = "flight_id")
    private Integer flightId;

    @Column(name = "flight_no")
    private String flightNo;

    @Column(name = "scheduled_departure")
    private LocalDateTime departureDate;

    @Column(name = "scheduled_arrival")
    private LocalDateTime arrivalDate;

    @Column(name = "departure_airport")
    private String airportFrom;

    @Column(name = "arrival_airport")
    private String airportTo;

    @Column(name = "status")
    private String status;

    @Column(name = "aircraft_code")
    private String aircraft;

    @Column(name = "actual_arrival")
    private LocalDateTime actualArrival;

    @Column(name = "actual_departure")
    private LocalDateTime actualDeparture;

    public Flights() {
    }

    @Override
    public String toString() {
        return "Flights{" +
                "flightId=" + flightId +
                ", flightNo='" + flightNo + '\'' +
                ", departureDate=" + departureDate +
                ", arrivalDate=" + arrivalDate +
                ", airportFrom='" + airportFrom + '\'' +
                ", airportTo='" + airportTo + '\'' +
                ", status='" + status + '\'' +
                ", aircraft='" + aircraft + '\'' +
                ", actualArrival=" + actualArrival +
                ", actualDeparture=" + actualDeparture +
                '}';
    }

    public Integer getFlightId() {
        return flightId;
    }

    public void setFlightId(Integer flightId) {
        this.flightId = flightId;
    }

    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDateTime scheduledDeparture) {
        this.departureDate = scheduledDeparture;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(LocalDateTime scheduledArrival) {
        this.arrivalDate = scheduledArrival;
    }

    public String getAirportFrom() {
        return airportFrom;
    }

    public void setAirportFrom(String departureAirport) {
        this.airportFrom = departureAirport;
    }

    public String getAirportTo() {
        return airportTo;
    }

    public void setAirportTo(String arrivalAirport) {
        this.airportTo = arrivalAirport;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAircraft() {
        return aircraft;
    }

    public void setAircraft(String aircraft) {
        this.aircraft = aircraft;
    }

    public LocalDateTime getActualArrival() {
        return actualArrival;
    }

    public void setActualArrival(LocalDateTime actualArrival) {
        this.actualArrival = actualArrival;
    }

    public LocalDateTime getActualDeparture() {
        return actualDeparture;
    }

    public void setActualDeparture(LocalDateTime actualDeparture) {
        this.actualDeparture = actualDeparture;
    }
}
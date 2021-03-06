package com.example.aviasale.domain.dto.apiDto;

import com.example.aviasale.domain.pojo.Price;
import com.example.aviasale.domain.entity.Airports;
import com.example.aviasale.domain.entity.Flights;

public class OneWayDirectFlightDto {
    private Boolean direct;
    private Flights flight;
    private Airports airportFrom;
    private Airports airportTo;
    private Price price;

    public OneWayDirectFlightDto() {
    }

    @Override
    public String toString() {
        return "OneWayDirectFlightDto{" +
                ", flight=" + flight +
                ", airportFrom=" + airportFrom +
                ", airportTo=" + airportTo +
                ", price=" + price +
                '}';
    }

    public Flights getFlight() {
        return flight;
    }

    public void setFlight(Flights flight) {
        this.flight = flight;
    }

    public Airports getAirportFrom() {
        return airportFrom;
    }

    public void setAirportFrom(Airports airportFrom) {
        this.airportFrom = airportFrom;
    }

    public Airports getAirportTo() {
        return airportTo;
    }

    public void setAirportTo(Airports airportTo) {
        this.airportTo = airportTo;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public Boolean getDirect() {
        return direct;
    }

    public void setDirect(Boolean direct) {
        this.direct = direct;
    }
}


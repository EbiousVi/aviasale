package com.example.aviasale.domain.dto.apiDto;

import com.example.aviasale.domain.entity.Airports;
import com.example.aviasale.domain.entity.Flights;
import com.example.aviasale.domain.pojo.Price;

public class ConnFlightDto {
    private Flights flight1;
    private Flights flight2;
    private Airports airFrom1;
    private Airports airTo1;
    private Airports airFrom2;
    private Airports airTo2;
    private Price price1;
    private Price price2;
    private Boolean conn;

    @Override
    public String toString() {
        return "ConnFlightDto{" +
                "flight1=" + flight1 +
                ", flight2=" + flight2 +
                ", airFrom1=" + airFrom1 +
                ", airTo1=" + airTo1 +
                ", airFrom2=" + airFrom2 +
                ", airTo2=" + airTo2 +
                ", price1=" + price1 +
                ", price2=" + price2 +
                ", isConnectionFlight=" + conn +
                '}';
    }

    public ConnFlightDto() {
    }

    public Flights getFlight1() {
        return flight1;
    }

    public void setFlight1(Flights flight1) {
        this.flight1 = flight1;
    }

    public Flights getFlight2() {
        return flight2;
    }

    public void setFlight2(Flights flight2) {
        this.flight2 = flight2;
    }

    public Airports getAirFrom1() {
        return airFrom1;
    }

    public void setAirFrom1(Airports airFrom1) {
        this.airFrom1 = airFrom1;
    }

    public Airports getAirTo1() {
        return airTo1;
    }

    public void setAirTo1(Airports airTo1) {
        this.airTo1 = airTo1;
    }

    public Airports getAirFrom2() {
        return airFrom2;
    }

    public void setAirFrom2(Airports airFrom2) {
        this.airFrom2 = airFrom2;
    }

    public Airports getAirTo2() {
        return airTo2;
    }

    public void setAirTo2(Airports airTo2) {
        this.airTo2 = airTo2;
    }

    public Price getPrice1() {
        return price1;
    }

    public void setPrice1(Price price1) {
        this.price1 = price1;
    }

    public Price getPrice2() {
        return price2;
    }

    public void setPrice2(Price price2) {
        this.price2 = price2;
    }

    public Boolean getConn() {
        return conn;
    }

    public void setConn(Boolean conn) {
        this.conn = conn;
    }
}

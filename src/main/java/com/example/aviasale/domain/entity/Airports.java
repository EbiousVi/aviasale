package com.example.aviasale.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
//In this table use VIEW from demo db to simplify
@Entity
@Table(name = "airports")
public class Airports {
    @Id
    @Column(name = "airport_code")
    private String airportCode;
    @Column(name = "airport_name")
    private String airportName;
    @Column(name = "city")
    private String city;
    @Column(name = "coordinates")
    private String point;
    @Column(name = "timezone")
    private String timeZone;

    public Airports() {
    }

    @Override
    public String toString() {
        return "Airports{" +
                "airportCode='" + airportCode + '\'' +
                ", airportName='" + airportName + '\'' +
                ", city='" + city + '\'' +
                ", point=" + point +
                ", timeZone='" + timeZone + '\'' +
                '}';
    }

    public String getAirportCode() {
        return airportCode;
    }

    public void setAirportCode(String airportCode) {
        this.airportCode = airportCode;
    }

    public String getAirportName() {
        return airportName;
    }

    public void setAirportName(String airportName) {
        this.airportName = airportName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }
}

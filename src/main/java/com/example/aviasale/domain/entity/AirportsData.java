package com.example.aviasale.domain.entity;

import com.example.aviasale.domain.custom_types.StringJsonUserType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "airports_data")
@Entity
@TypeDefs({@TypeDef(name = "StringJsonObject", typeClass = StringJsonUserType.class)})
public class AirportsData {
    @Id
    @Column(name = "airport_code")
    private String airportCode;

    @Column(name = "airport_name")
    @Type(type = "StringJsonObject")
    private String airportName;

    @Column(name = "city")
    @Type(type = "StringJsonObject")
    private String city;

    @Column(name = "coordinates")
    private String point;
    @Column(name = "timezone")
    private String timeZone;

    public AirportsData() {
    }

    @Override
    public String toString() {
        return "AirportsData{" +
                "airportCode='" + airportCode + '\'' +
                ", airportName='" + airportName + '\'' +
                ", city='" + city + '\'' +
                ", point='" + point + '\'' +
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

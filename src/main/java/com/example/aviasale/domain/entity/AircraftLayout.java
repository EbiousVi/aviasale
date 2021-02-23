package com.example.aviasale.domain.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "seats")
public class AircraftLayout {

    @Embeddable
    public static class CompositeKey implements Serializable {

        @Column(name = "aircraft_code")
        private String aircraftCode;

        @Column(name = "seat_no")
        private String seatNumber;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CompositeKey that = (CompositeKey) o;
            return Objects.equals(aircraftCode, that.aircraftCode) &&
                    Objects.equals(seatNumber, that.seatNumber);
        }

        @Override
        public int hashCode() {
            return Objects.hash(aircraftCode, seatNumber);
        }

        public String getAircraftCode() {
            return aircraftCode;
        }

        public void setAircraftCode(String aircraftCode) {
            this.aircraftCode = aircraftCode;
        }

        public String getSeatNumber() {
            return seatNumber;
        }

        public void setSeatNumber(String seatNumber) {
            this.seatNumber = seatNumber;
        }
    }

    @EmbeddedId
    private CompositeKey compositeKey;

    @Column(name = "fare_conditions")
    private String fareConditions;

    public AircraftLayout() {
    }

    public CompositeKey getCompositeKey() {
        return compositeKey;
    }

    public void setCompositeKey(CompositeKey compositeKey) {
        this.compositeKey = compositeKey;
    }

    public String getFareConditions() {
        return fareConditions;
    }

    public void setFareConditions(String fareConditions) {
        this.fareConditions = fareConditions;
    }
}

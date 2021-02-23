package com.example.aviasale.domain.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "ticket_flights")
public class TicketFlights {
    @EmbeddedId
    private CompositeKey compositeKey;
    @Column(name = "fare_conditions")
    private String fareConditions;
    @Column(name = "amount")
    private Double amount;

    @Embeddable
    public static class CompositeKey implements Serializable {

        @Column(name = "ticket_no")
        private String ticketNumber;

        @Column(name = "flight_id")
        private Integer flightId;


        public CompositeKey() {
        }

        @Override
        public String toString() {
            return "CompositeKey{" +
                    "ticketNumber='" + ticketNumber + '\'' +
                    ", flightId=" + flightId +
                    '}';
        }

        public String getTicketNumber() {
            return ticketNumber;
        }

        public void setTicketNumber(String ticketNumber) {
            this.ticketNumber = ticketNumber;
        }

        public Integer getFlightId() {
            return flightId;
        }

        public void setFlightId(Integer flightId) {
            this.flightId = flightId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CompositeKey that = (CompositeKey) o;
            return Objects.equals(ticketNumber, that.ticketNumber) &&
                    Objects.equals(flightId, that.flightId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(ticketNumber, flightId);
        }
    }

    public TicketFlights() {
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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "TicketFlights{" +
                "compositeKey=" + compositeKey +
                ", fareConditions='" + fareConditions + '\'' +
                ", amount=" + amount +
                '}';
    }
}

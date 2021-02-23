package com.example.aviasale.domain.pojo;

import java.math.BigDecimal;

public class Price {
    private Integer flightId;
    private BigDecimal value;

    @Override
    public String toString() {
        return "Price{" +
                "flightId=" + flightId +
                ", value=" + value +
                '}';
    }

    public Price() {
    }

    public Integer getFlightId() {
        return flightId;
    }

    public void setFlightId(Integer flightId) {
        this.flightId = flightId;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}

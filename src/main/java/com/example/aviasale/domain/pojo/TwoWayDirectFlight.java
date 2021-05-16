package com.example.aviasale.domain.pojo;

import com.example.aviasale.domain.entity.Flights;

public class TwoWayDirectFlight {
    private final Flights flightThere;
    private final Flights flightBack;

    public TwoWayDirectFlight(Flights flightThere, Flights flightBack) {
        this.flightThere = flightThere;
        this.flightBack = flightBack;
    }

    public Flights getFlightThere() {
        return flightThere;
    }

    public Flights getFlightBack() {
        return flightBack;
    }
}

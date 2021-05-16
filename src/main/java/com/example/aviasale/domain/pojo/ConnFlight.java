package com.example.aviasale.domain.pojo;

import com.example.aviasale.domain.entity.Flights;

public class ConnFlight {
    private final Flights first;
    private final Flights second;

    public ConnFlight(Flights first, Flights second) {
        this.first = first;
        this.second = second;
    }

    public Flights getFirst() {
        return first;
    }

    public Flights getSecond() {
        return second;
    }


}

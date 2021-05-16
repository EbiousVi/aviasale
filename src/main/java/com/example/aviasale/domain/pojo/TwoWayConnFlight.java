package com.example.aviasale.domain.pojo;

public class TwoWayConnFlight {
    private final ConnFlight there;
    private final ConnFlight back;

    public TwoWayConnFlight(ConnFlight there, ConnFlight back) {
        this.there = there;
        this.back = back;
    }

    public ConnFlight getThere() {
        return there;
    }

    public ConnFlight getBack() {
        return back;
    }


}

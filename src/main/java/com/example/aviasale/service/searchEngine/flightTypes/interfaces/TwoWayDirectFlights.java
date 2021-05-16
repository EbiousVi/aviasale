package com.example.aviasale.service.searchEngine.flightTypes.interfaces;

import com.example.aviasale.domain.entity.Flights;

import java.util.List;

public interface TwoWayDirectFlights<T> extends DirectFlights<T>, TwoWayFlights {
    List<Flights> findFlightsThere();

    List<Flights> findFlightsBack();
}

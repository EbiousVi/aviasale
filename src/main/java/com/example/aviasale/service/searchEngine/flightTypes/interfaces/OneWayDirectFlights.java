package com.example.aviasale.service.searchEngine.flightTypes.interfaces;

import com.example.aviasale.domain.entity.Flights;

import java.util.List;

public interface OneWayDirectFlights<T> extends DirectFlights<T>, OneWayFlights {
    List<Flights> findFlights();
}

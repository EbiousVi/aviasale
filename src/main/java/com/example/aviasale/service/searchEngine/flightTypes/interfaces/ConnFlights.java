package com.example.aviasale.service.searchEngine.flightTypes.interfaces;

import com.example.aviasale.domain.entity.Flights;

import java.util.List;

public interface ConnFlights<T> extends FlightType {
    List<T> durationFilter(List<Flights> first, List<Flights> second);

    List<T> freeSeatsFilter(List<T> flights);
}

package com.example.aviasale.service.searchEngine.flightTypes.directFlightImpl;

import com.example.aviasale.domain.entity.Flights;
import com.example.aviasale.service.searchEngine.flightTypes.interfaces.TwoWayDirectFlights;

import java.util.List;

public class TwoWayDirectFlightsService implements TwoWayDirectFlights<TwoWayDirectFlights> {
    @Override
    public List<Flights> findFlightsThere() {
        return null;
    }

    @Override
    public List<Flights> findFlightsBack() {
        return null;
    }

    @Override
    public List<TwoWayDirectFlights> freeSeatsFilter(List<TwoWayDirectFlights> flights) {
        return null;
    }

    @Override
    public List<?> getFlight() {
        return null;
    }
}

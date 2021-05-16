package com.example.aviasale.service.searchEngine.flightTypes.interfaces;

import java.util.List;

public interface DirectFlights<T> extends FlightType {
    List<T> freeSeatsFilter(List<T> flights);
}

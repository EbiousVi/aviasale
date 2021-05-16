package com.example.aviasale.service.searchEngine.flightTypes.interfaces;

import com.example.aviasale.domain.entity.Flights;

import java.util.List;

public interface OneWayConnFlights<T> extends ConnFlights<T>, OneWayFlights {
    List<Flights> findFirst(List<String> airportsInCityFrom, List<String> intersectArrivalAirports);

    List<Flights> findSecond(List<String> intersectArrivalAirports, List<String> airportsInCityTo);
}

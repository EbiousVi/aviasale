package com.example.aviasale.service.searchEngine.flightTypes.interfaces;

import com.example.aviasale.domain.entity.Flights;

import java.util.List;

public interface TwoWayConnFlights<T> extends ConnFlights<T>, TwoWayFlights {
    List<Flights> findFirstThere();

    List<Flights> findSecondThere();

    List<Flights> findFirstBack();

    List<Flights> findSecondBack();
}

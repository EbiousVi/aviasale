package com.example.aviasale.service.searchEngine.searchEngine;

import com.example.aviasale.service.searchEngine.flightTypes.interfaces.OneWayFlights;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TwoWaySearchEngineService implements SearchEngine {

    @Autowired
    private List<OneWayFlights> twoWayServices;

    @Override
    public ResponseEntity<?> getResult() {
        for (OneWayFlights strategy : twoWayServices) {
            List<?> flights = strategy.getFlight();
            if (!flights.isEmpty()) {
                return new ResponseEntity<>(flights, HttpStatus.OK);
            }
        }
        String message = "Flights Not Found By Search Query Param! Check the parameters";
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }
}


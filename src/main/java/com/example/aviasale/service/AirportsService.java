package com.example.aviasale.service;

import com.example.aviasale.domain.entity.Airports;
import com.example.aviasale.expection.AirportsNotFoundException;
import com.example.aviasale.repository.AirportsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AirportsService {
    final private AirportsRepository airportsRepository;

    @Autowired
    public AirportsService(AirportsRepository airportsRepository) {
        this.airportsRepository = airportsRepository;
    }


    public List<String> getAllAirportsInCity(String city) throws AirportsNotFoundException {
        List<Airports> airports = airportsRepository.findAllByCityContainsIgnoreCase(city)
                .orElseThrow(() -> new AirportsNotFoundException("AirportsData not found by city = " + city, HttpStatus.NOT_FOUND));
        return airports.stream().map(Airports::getAirportCode).collect(Collectors.toList());
    }

    public Airports getAirportFrom(Integer flightId) throws AirportsNotFoundException {
        return airportsRepository.findAirportFromByFlightId(flightId)
                .orElseThrow(() -> new AirportsNotFoundException("AirportData not found by flightId = " + flightId, HttpStatus.NOT_FOUND));
    }

    public Airports getAirportTo(Integer flightId) throws AirportsNotFoundException {
        return airportsRepository.findAirportToByFlightId(flightId)
                .orElseThrow(() -> new AirportsNotFoundException("AirportData not found by flightId = " + flightId, HttpStatus.NOT_FOUND));
    }

    public List<Airports> getAllAirportsFromDB() {
        return airportsRepository.findAllBy().orElse(Collections.emptyList());
    }
}

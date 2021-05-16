package com.example.aviasale.service;

import com.example.aviasale.domain.entity.Airports;
import com.example.aviasale.repository.AirportsRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<String> getAllAirportsInCity(String city) {
        List<Airports> airports = airportsRepository.findAllByCityContainsIgnoreCase(city);
        return airports.stream().map(Airports::getAirportCode).collect(Collectors.toList());
    }

    public Airports getAirportFrom(Integer flightId) {
        return airportsRepository.findAirportFromByFlightId(flightId).orElse(new Airports());
    }

    public Airports getAirportTo(Integer flightId) {
        return airportsRepository.findAirportToByFlightId(flightId).orElse(new Airports());
    }

    public List<Airports> getAllAirportsFromDB() {
        return airportsRepository.findAllBy().orElse(Collections.emptyList());
    }
}

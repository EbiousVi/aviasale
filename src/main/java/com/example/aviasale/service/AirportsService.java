package com.example.aviasale.service;

import com.example.aviasale.domain.entity.Airports;
import com.example.aviasale.repository.AirportsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AirportsService {
    final private AirportsRepository airportsRepository;

    @Autowired
    public AirportsService(AirportsRepository airportsRepository) {
        this.airportsRepository = airportsRepository;
    }

    public List<String> getAllAirportsInCity(String city) {
        List<Airports> airports = airportsRepository.findAllByCityContainsIgnoreCase(city);
        List<String> list = new ArrayList<>();
        for (Airports airport : airports) {
            list.add(airport.getAirportCode());
        }
        return list;
    }

    public Airports getAirportFrom(Integer flightId) {
        return airportsRepository.findAirportFromByFlightId(flightId);
    }

    public Airports getAirportTo(Integer flightId) {
        return airportsRepository.findAirportToByFlightId(flightId);
    }

    public List<Airports> allAirports() {
        return airportsRepository.findAllBy();
    }
}

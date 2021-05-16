package com.example.aviasale.service;

import com.example.aviasale.domain.entity.Flights;
import com.example.aviasale.expection.FlightsNotFoundException;
import com.example.aviasale.repository.FlightsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FlightsService {

    private final FlightsRepository flightsRepository;

    public FlightsService(FlightsRepository flightsRepository) {
        this.flightsRepository = flightsRepository;
    }

    public Flights getFlightByTicketNo(String ticketNo) throws FlightsNotFoundException {
        return flightsRepository.findByTicketNo(ticketNo)
                .orElseThrow(() -> new FlightsNotFoundException("Flight not found by ticketNo = " + ticketNo, HttpStatus.NOT_FOUND));
    }

    public Flights getFlightByFlightId(Integer flightId) throws FlightsNotFoundException {
        return flightsRepository.findByFlightId(flightId)
                .orElseThrow(() -> new FlightsNotFoundException("Flight not found by flightId = " + flightId, HttpStatus.NOT_FOUND));
    }

    public List<Flights> getFlightsByParam(List<String> airportsInCityFrom,
                                           List<String> airportsInCityTo,
                                           LocalDateTime dayStart,
                                           LocalDateTime dayEnd) {
        return flightsRepository.findFlightsBySearchParams(airportsInCityFrom, airportsInCityTo, dayStart, dayEnd);
    }

    public List<String> getIntersectArrivalAirports(List<String> airportsInCityFrom,
                                                    List<String> airportsInCityTo) {
        return flightsRepository.findIntersectArrivalAirports(airportsInCityFrom, airportsInCityTo);
    }

    public List<Flights> findAllFlights() {
        return flightsRepository.findAllFlights();
    }
}

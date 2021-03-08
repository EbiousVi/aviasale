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
                                           LocalDateTime date1,
                                           LocalDateTime date2) throws FlightsNotFoundException {
        return flightsRepository.findFlightsWithSearchParams(airportsInCityFrom, airportsInCityTo, date1, date2)
                .orElseThrow(() -> new FlightsNotFoundException("Flights not found by search params", HttpStatus.NOT_FOUND));
    }

    public List<String> getIntersectArrivalAirports(List<String> airportsInCityFrom,
                                                    List<String> airportsInCityTo) throws FlightsNotFoundException {
        return flightsRepository.findIntersectArrivalAirports(airportsInCityFrom, airportsInCityTo)
                .orElseThrow(() -> new FlightsNotFoundException("No crossing arrival airports", HttpStatus.NOT_FOUND));
    }
}

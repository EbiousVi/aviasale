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
        return flightsRepository.findFlightByTicketNo(ticketNo)
                .orElseThrow(() -> new FlightsNotFoundException("Flight not found by TicketNo" + ticketNo, HttpStatus.NOT_FOUND));
    }

    public Flights getFlightByFlightId(Integer flightId) throws FlightsNotFoundException {
        return flightsRepository.findByFlightId(flightId)
                .orElseThrow(() -> new FlightsNotFoundException("Flight not found by TicketNo" + flightId, HttpStatus.NOT_FOUND));
    }

    public List<Flights> getFlightsByParam(List<String> airportsInCityFrom,
                                           List<String> airportsInCityTo,
                                           LocalDateTime date1,
                                           LocalDateTime date2) throws FlightsNotFoundException {
        return flightsRepository.findFlightsWithSearchParams(airportsInCityFrom, airportsInCityTo, date1, date2)
                .orElseThrow(() -> new FlightsNotFoundException("Flights not found by Search Params", HttpStatus.NOT_FOUND));
    }

    public List<String> intersectArrivalAirports(List<String> airportsInCityFrom, List<String> airportsInCityTo) throws FlightsNotFoundException {
        return flightsRepository.intersectArrivalAirports(airportsInCityFrom, airportsInCityTo)
                .orElseThrow(() -> new FlightsNotFoundException("No crossing arrival Airports", HttpStatus.NOT_FOUND));
    }
}

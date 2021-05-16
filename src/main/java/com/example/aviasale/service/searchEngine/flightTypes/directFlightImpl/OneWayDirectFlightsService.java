package com.example.aviasale.service.searchEngine.flightTypes.directFlightImpl;

import com.example.aviasale.domain.dto.apiDto.OneWayDirectFlightDto;
import com.example.aviasale.domain.entity.Flights;
import com.example.aviasale.service.AirportsService;
import com.example.aviasale.service.FlightsService;
import com.example.aviasale.service.SearchQueryService;
import com.example.aviasale.service.TicketFlightsService;
import com.example.aviasale.service.dtoPacker.OneWayDirectFlightPackager;
import com.example.aviasale.service.searchEngine.flightTypes.interfaces.OneWayDirectFlights;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Order(1)
public class OneWayDirectFlightsService implements OneWayDirectFlights<Flights> {

    private final FlightsService flightsService;
    private final AirportsService airportsService;
    private final SearchQueryService searchQuery;
    private final TicketFlightsService ticketFlightsService;
    private final OneWayDirectFlightPackager packager;

    @Autowired
    public OneWayDirectFlightsService(FlightsService flightsService,
                                      AirportsService airportsService,
                                      SearchQueryService searchQuery,
                                      TicketFlightsService ticketFlightsService,
                                      OneWayDirectFlightPackager packager) {
        this.flightsService = flightsService;
        this.airportsService = airportsService;
        this.searchQuery = searchQuery;
        this.ticketFlightsService = ticketFlightsService;
        this.packager = packager;
    }

    @Override
    public List<Flights> findFlights() {
        List<String> airportsInCityFrom = airportsService.getAllAirportsInCity(searchQuery.getSearchQueryDto().getCityFrom());
        List<String> airportsInCityTo = airportsService.getAllAirportsInCity(searchQuery.getSearchQueryDto().getCityTo());
        return flightsService.getFlightsByParam(airportsInCityFrom, airportsInCityTo,
                searchQuery.searchDateStart(), searchQuery.searchDateEnd());
    }

    /**
     * Check available seats. If number of tickets from request less or equal number of available seats
     * then flight fits the condition.
     */
    @Transactional
    public List<Flights> freeSeatsFilter(List<Flights> flights) {
        if (flights.isEmpty()) return Collections.emptyList();
        return flights.stream().filter(flight -> {
            Integer availableSeats = ticketFlightsService.getFreeSeats(
                    flight.getFlightId(),
                    flight.getAircraft(),
                    searchQuery.getSearchQueryDto().getConditions());
            return availableSeats >= searchQuery.getSearchQueryDto().getNumberOfTickets();
        }).collect(Collectors.toList());
    }

    @Override
    public List<?> getFlight() {
        List<Flights> flights = freeSeatsFilter(findFlights());
        List<OneWayDirectFlightDto> dtoList = packager.wrapToDto(flights);
        if (dtoList.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        return dtoList;
    }
}

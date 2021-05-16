package com.example.aviasale.service.searchEngine.flightTypes.connFlightImpl;

import com.example.aviasale.domain.dto.apiDto.OneWayConnFlightDto;
import com.example.aviasale.domain.entity.Flights;
import com.example.aviasale.domain.pojo.ConnFlight;
import com.example.aviasale.service.*;
import com.example.aviasale.service.FlightsService;
import com.example.aviasale.service.dtoPacker.OneWayConnFlightPackager;
import com.example.aviasale.service.searchEngine.flightTypes.interfaces.OneWayConnFlights;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Order(2)
public class OneWayConnFlightsService implements OneWayConnFlights<ConnFlight> {
    private final FlightsService flightsService;
    private final AirportsService airportsService;
    private final TicketFlightsService ticketFlightsService;
    private final SearchQueryService searchQuery;
    private final OneWayConnFlightPackager packager;

    @Autowired
    public OneWayConnFlightsService(FlightsService flightsService, AirportsService airportsService,
                                    TicketFlightsService ticketFlightsService, SearchQueryService searchQuery,
                                    OneWayConnFlightPackager packager) {
        this.flightsService = flightsService;
        this.airportsService = airportsService;
        this.ticketFlightsService = ticketFlightsService;
        this.searchQuery = searchQuery;
        this.packager = packager;
    }

    @Override
    public List<Flights> findFirst(List<String> airportsInCityFrom, List<String> intersectArrivalAirports) {
        return flightsService.getFlightsByParam(airportsInCityFrom, intersectArrivalAirports,
                searchQuery.searchDateStart(), searchQuery.searchDateEnd());
    }

    @Override
    public List<Flights> findSecond(List<String> intersectArrivalAirports, List<String> airportsInCityTo) {
        return flightsService.getFlightsByParam(intersectArrivalAirports, airportsInCityTo,
                searchQuery.searchDateStart(), searchQuery.searchDateEnd().plusDays(1));
    }

    @Override
    public List<ConnFlight> durationFilter(List<Flights> first, List<Flights> second) {
        if (!first.isEmpty() && !second.isEmpty()) {
            List<ConnFlight> connFlights = new ArrayList<>();
            long prevDiff = 24;
            for (Flights f : first) {
                for (Flights s : second) {
                    Duration duration = Duration.between(f.getArrivalDate(), s.getDepartureDate());
                    long dur = duration.toHours();
                    if (dur >= 1 && dur < 24 && dur <= prevDiff) {
                        prevDiff = dur;
                        connFlights.add(new ConnFlight(f, s));
                    }
                }
            }
            if (!connFlights.isEmpty()) return connFlights;
        }
        return Collections.emptyList();
    }

    @Override
    public List<ConnFlight> freeSeatsFilter(List<ConnFlight> flights) {
        if (flights.isEmpty()) return Collections.emptyList();
        return flights.stream().filter(conn -> {
            Integer firstFreeSeats = ticketFlightsService.getFreeSeats(conn.getFirst().getFlightId(),
                    conn.getFirst().getAircraft(),
                    searchQuery.getSearchQueryDto().getConditions());
            Integer secondFreeSeats = ticketFlightsService.getFreeSeats(conn.getSecond().getFlightId(),
                    conn.getSecond().getAircraft(),
                    searchQuery.getSearchQueryDto().getConditions());
            Integer numberOfTickets = searchQuery.getSearchQueryDto().getNumberOfTickets();
            return firstFreeSeats >= numberOfTickets && secondFreeSeats >= numberOfTickets;
        }).collect(Collectors.toList());
    }

    @Override
    public List<?> getFlight() {
        List<String> airportsInCityFrom = airportsService.getAllAirportsInCity(searchQuery.getSearchQueryDto().getCityFrom());
        List<String> airportsInCityTo = airportsService.getAllAirportsInCity(searchQuery.getSearchQueryDto().getCityTo());
        List<String> intersectArrivalAirports = flightsService.getIntersectArrivalAirports(airportsInCityFrom, airportsInCityTo);

        List<Flights> first = findFirst(airportsInCityFrom, intersectArrivalAirports);
        List<Flights> second = findSecond(intersectArrivalAirports, airportsInCityTo);

        List<ConnFlight> connFlights = freeSeatsFilter(durationFilter(first, second));
        List<OneWayConnFlightDto> dtoList = packager.wrapToDto(connFlights);
        if (dtoList.isEmpty()) return Collections.emptyList();
        return dtoList;
    }
}

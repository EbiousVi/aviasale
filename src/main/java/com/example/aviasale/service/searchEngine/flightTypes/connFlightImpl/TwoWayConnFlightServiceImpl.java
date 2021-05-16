/*
package com.example.aviasale.service.searchEngine.flightTypes.connFlightImpl;

import com.example.aviasale.domain.dto.apiDto.OneWayConnFlightDto;
import com.example.aviasale.domain.entity.Flights;
import com.example.aviasale.domain.pojo.TwoWayConnFlight;
import com.example.aviasale.service.AirportsService;
import com.example.aviasale.service.PriceService;
import com.example.aviasale.service.SearchQueryService;
import com.example.aviasale.service.TicketFlightsService;
import com.example.aviasale.service.searchEngine.flightTypes.interfaces.ConnFlights;
import com.example.aviasale.service.FlightsService;
import com.example.aviasale.service.dtoPacker.OneWayConnFlightPackager;
import com.example.aviasale.service.searchEngine.flightTypes.interfaces.TwoWayConnFlights;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TwoWayConnFlightServiceImpl implements TwoWayConnFlights<TwoWayConnFlight> {
    private final FlightsService flightsService;
    private final AirportsService airportsService;
    private final TicketFlightsService ticketFlightsService;
    private final SearchQueryService searchQueryService;
    private final OneWayConnFlightPackager packager;
    //private final List<String> MOSCOW_AIRPORTS = Arrays.asList("SVO", "DME", "VKO");

    @Autowired
    public TwoWayConnFlightServiceImpl(FlightsService flightsService, AirportsService airportsService, TicketFlightsService ticketFlightsService, PriceService priceService, SearchQueryService searchQueryService, OneWayConnFlightPackager packager) {
        this.flightsService = flightsService;
        this.airportsService = airportsService;
        this.ticketFlightsService = ticketFlightsService;
        this.searchQueryService = searchQueryService;
        this.packager = packager;
    }

    @Override
    public List<List<Flights>> findFlight() {
        List<String> airportsInCityFrom = airportsService.getAllAirportsInCity(searchQueryService.getSearchFormDto().getCityFrom());
        List<String> airportsInCityTo = airportsService.getAllAirportsInCity(searchQueryService.getSearchFormDto().getCityTo());
        List<String> intersectArrivalAirports = flightsService.getIntersectArrivalAirports(airportsInCityFrom, airportsInCityTo);
        if (intersectArrivalAirports.isEmpty()) {
            return Collections.emptyList();
        }
        List<Flights> first = flightsService.getFlightsByParam(airportsInCityFrom, intersectArrivalAirports,
                searchQueryService.searchDateStart(), searchQueryService.searchDateEnd());
        List<Flights> second = flightsService.getFlightsByParam(intersectArrivalAirports, airportsInCityTo,
                searchQueryService.searchDateStart(), searchQueryService.searchDateEnd().plusDays(1));
        return Arrays.asList(first, second);
    }

    @Override
    public List<TwoWayConnFlights> durationFilter(List<List<Flights>> flights) {
        List<Flights> first = flights.get(0);
        List<Flights> second = flights.get(1);

        if (!first.isEmpty() && !second.isEmpty()) {
            List<ConnFlights> connFlights = new ArrayList<>();
            long prevDiff = 24;
            for (Flights f : first) {
                for (Flights s : second) {
                    Duration duration = Duration.between(f.getArrivalDate(), s.getDepartureDate());
                    long dur = duration.toHours();
                    if (dur >= 1 && dur < 24 && dur <= prevDiff) {
                        prevDiff = dur;
                        connFlights.add(new ConnFlights(f, s));
                    }
                }
            }
            if (!connFlights.isEmpty()) return connFlights;
        }
        return Collections.emptyList();
    }

    @Override
    public List<TwoWayConnFlights> freeSeatsFilter(List<TwoWayConnFlights> flights) {
        if (flights.isEmpty()) return Collections.emptyList();
        return flights.stream().filter(pair -> {
            Integer firstFlightFreeSeats = ticketFlightsService.getFreeSeats(pair.getFirst().getFlightId(),
                    pair.getFirst().getAircraft(),
                    searchQueryService.getSearchFormDto().getConditions());
            Integer secondFlightFreeSeats = ticketFlightsService.getFreeSeats(pair.getSecond().getFlightId(),
                    pair.getSecond().getAircraft(),
                    searchQueryService.getSearchFormDto().getConditions());
            return firstFlightFreeSeats > 0 && secondFlightFreeSeats > 0;
        }).collect(Collectors.toList());
    }

    @Override
    public List<?> getFlight() {
        List<TwoWayConnFlights> connFlights = freeSeatsFilter(durationFilter(findFlight()));
        return null;
    }
}

    public List<TwoWayConnFlights> m() {
        List<ConnFlights> connFlightsThere = freeSeatsFilter(durationFilter(getFlight()));
        List<ConnFlights> connFlightsBack = new ArrayList<>();
        if (!connFlightsThere.isEmpty()) {
            connFlightsBack = freeSeatsFilter(durationFilter(getFlight()));
        }
        List<TwoWayConnFlights> result = new ArrayList<>();


        for (ConnFlights there : connFlightsThere) {
            for (ConnFlights back : connFlightsBack) {
                Duration duration = Duration.between(there.getSecond().getArrivalDate(),
                        back.getFirst().getDepartureDate());
                if (duration.toHours() > 1) {
                    result.add(new TwoWayConnFlights(there, back));
                }
            }
        }
        return result;
    }

    public Comparator<OneWayConnFlightDto> connFlightsComparatorByDuration() {
        return (o1, o2) -> {
            Duration duration1 = Duration.between(o1.getFlight1().getDepartureDate(), o1.getFlight2().getArrivalDate());
            Duration duration2 = Duration.between(o2.getFlight1().getDepartureDate(), o2.getFlight2().getArrivalDate());
            return Long.compare(duration1.toHours(), duration2.toHours());
        };
    }
*/

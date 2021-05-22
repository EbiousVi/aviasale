package com.example.aviasale.controller;

import com.example.aviasale.domain.entity.Airports;
import com.example.aviasale.service.AirportsService;
import com.example.aviasale.service.SearchQueryService;
import com.example.aviasale.service.TicketFlightsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class InfoController {
    private final AirportsService airportsService;
    private final TicketFlightsService ticketFlightsService;
    private final SearchQueryService searchQuery;

    @Autowired
    public InfoController(AirportsService airportsService, TicketFlightsService ticketFlightsService,
                          SearchQueryService searchQuery) {
        this.airportsService = airportsService;
        this.ticketFlightsService = ticketFlightsService;
        this.searchQuery = searchQuery;
    }

    @GetMapping("/airports")
    public List<String> airports() {
        List<Airports> list = airportsService.getAllAirportsFromDB();
        return list.stream().map(Airports::getCity).sorted().distinct().collect(Collectors.toList());
    }

    @GetMapping("/freeSeats")
    public Integer getFreeSeats(@RequestParam("flightId") Integer flightId,
                                @RequestParam("aircraft") String aircraft) {
        return ticketFlightsService.getFreeSeats(flightId, aircraft, searchQuery.getSearchQueryDto().getConditions());
    }

}

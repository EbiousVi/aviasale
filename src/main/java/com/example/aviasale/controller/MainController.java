package com.example.aviasale.controller;

import com.example.aviasale.domain.dto.apiDto.BookingsDto;
import com.example.aviasale.domain.dto.apiDto.PassengersDto;
import com.example.aviasale.domain.dto.apiDto.SearchFormDto;
import com.example.aviasale.domain.entity.Airports;
import com.example.aviasale.domain.entity.Flights;
import com.example.aviasale.domain.entity.User;
import com.example.aviasale.domain.pojo.Price;
import com.example.aviasale.expection.*;
import com.example.aviasale.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class MainController {
    private final SearchEngineService searchEngineService;
    private final AirportsService airportsService;
    private final RegBookingService regBookingService;
    private final UserService userService;
    private final TicketFlightsService ticketFlightsService;
    private final FlightsService flightsService;
    private final BookingsService bookingsService;

    private List<Price> price;
    private SearchFormDto searchFormDto;

    @Autowired
    public MainController(SearchEngineService searchEngineService, AirportsService airportsService,
                          RegBookingService regBookingService, UserService userService,
                          TicketFlightsService ticketFlightsService, FlightsService flightsService,
                          BookingsService bookingsService) {
        this.searchEngineService = searchEngineService;
        this.airportsService = airportsService;
        this.regBookingService = regBookingService;
        this.userService = userService;
        this.ticketFlightsService = ticketFlightsService;
        this.flightsService = flightsService;
        this.bookingsService = bookingsService;
    }

    @GetMapping("/airports")
    public List<String> airports() {
        List<Airports> list = airportsService.getAllAirportsFromDB();
        return list.stream().map(Airports::getCity).sorted().distinct().collect(Collectors.toList());
    }

    @GetMapping("/freeSeats")
    public Integer getFreeSeats(@RequestParam("flightId") Integer flightId, @RequestParam("aircraft") String aircraft) throws TicketFlightsNotFoundException {
        return ticketFlightsService.getFreeSeats(flightId, aircraft, searchFormDto.getConditions());
    }

    @GetMapping("/bookings")
    public List<BookingsDto> getBookingsByEmail(Principal principal) throws TicketsNotFoundException {
        User user = userService.getUserByEmail(principal.getName());
        return regBookingService.getAllBookingDtoByUser(user);
    }

    @PostMapping("/flights")
    public ResponseEntity<?> getFlights(@RequestBody SearchFormDto searchFormDto) throws CustomException {
        this.searchFormDto = searchFormDto;
        searchEngineService.setSearchFormDto(searchFormDto);
        return searchEngineService.getFlights();
    }

    @PostMapping("/prepare-booking")
    @ResponseStatus(code = HttpStatus.OK)
    public void prepareBooking(@RequestBody List<Price> price) {
        this.price = price;
    }

    @PostMapping("/booking")
    public List<BookingsDto> booking(@RequestBody List<PassengersDto> passengersDto, Principal principal) throws TicketsNotFoundException, BookingFailedException, FlightsNotFoundException, TicketFlightsNotFoundException {
        User user = userService.getUserByEmail(principal.getName());
        regBookingService.registrationBooking(user, passengersDto, searchFormDto, price);
        return regBookingService.getAllBookingDtoByUser(user);
    }

    @DeleteMapping("/delete-booking/{bookRef}")
    public void deleteBooking(@PathVariable("bookRef") String bookRef) throws TicketsNotFoundException, TicketFlightsNotFoundException {
        bookingsService.deleteBooking(regBookingService.deleteBookingRelations(bookRef));
    }

    @DeleteMapping("/remove-passenger/{ticketNumber}")
    public void removePassenger(@PathVariable("ticketNumber") String ticketNumber) throws TicketFlightsNotFoundException {
        regBookingService.removePassenger(ticketNumber);
    }

    @GetMapping("/booking/details")
    public Flights getFlightDetails(@RequestParam String ticketNo) throws FlightsNotFoundException {
        return flightsService.getFlightByTicketNo(ticketNo);
    }
}

package com.example.aviasale.controller;

import com.example.aviasale.domain.dto.apiDto.BookingsDto;
import com.example.aviasale.domain.dto.apiDto.PassengersDto;
import com.example.aviasale.domain.entity.Flights;
import com.example.aviasale.domain.entity.User;
import com.example.aviasale.domain.pojo.Price;
import com.example.aviasale.expection.*;
import com.example.aviasale.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
public class BookingController {
    private final RegBookingService regBookingService;
    private final UserService userService;
    private final FlightsService flightsService;
    private final BookingsService bookingsService;
    private final SearchQueryService searchQuery;


    public BookingController(RegBookingService regBookingService, BookingsService bookingsService, UserService userService,
                             FlightsService flightsService, SearchQueryService searchQuery) {
        this.regBookingService = regBookingService;
        this.userService = userService;
        this.flightsService = flightsService;
        this.bookingsService = bookingsService;
        this.searchQuery = searchQuery;
    }

    @GetMapping("/bookings")
    public List<BookingsDto> getBookingsByEmail(Principal principal) throws TicketsNotFoundException {
        User user = userService.getUserByEmail(principal.getName());
        return regBookingService.getAllBookingDtoByUser(user);
    }

    @PostMapping("/prepare-booking")
    @ResponseStatus(code = HttpStatus.OK)
    public void prepareBooking(@RequestBody List<Price> prices) {
        searchQuery.setPrices(prices);
    }

    @PostMapping("/booking")
    public List<BookingsDto> booking(@RequestBody List<PassengersDto> passengersDto, Principal principal) throws CustomException {
        User user = userService.getUserByEmail(principal.getName());
        regBookingService.prepareBooking(user, passengersDto);
        return regBookingService.getAllBookingDtoByUser(user);
    }

    @DeleteMapping("/delete-booking/{bookRef}")
    public void deleteBooking(@PathVariable("bookRef") String bookRef) throws TicketFlightsNotFoundException {
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

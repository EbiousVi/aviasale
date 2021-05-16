package com.example.aviasale.controller;

import com.example.aviasale.domain.dto.apiDto.BookingsDto;
import com.example.aviasale.domain.entity.Flights;
import com.example.aviasale.domain.entity.User;
import com.example.aviasale.expection.FlightsNotFoundException;
import com.example.aviasale.expection.TicketFlightsNotFoundException;
import com.example.aviasale.expection.TicketsNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController

public class BookingController {

    /*@GetMapping("/bookings")
    public List<BookingsDto> getBookingsByEmail(Principal principal) throws TicketsNotFoundException {
        User user = userService.getUserByEmail(principal.getName());
        return regBookingService.getAllBookingDtoByUser(user);
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
    }*/
}

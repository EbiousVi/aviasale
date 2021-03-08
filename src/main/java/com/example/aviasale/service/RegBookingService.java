package com.example.aviasale.service;

import com.example.aviasale.domain.dto.apiDto.PassengersDto;
import com.example.aviasale.domain.pojo.Price;
import com.example.aviasale.domain.dto.apiDto.BookingsDto;
import com.example.aviasale.domain.dto.apiDto.SearchFormDto;
import com.example.aviasale.domain.entity.*;
import com.example.aviasale.expection.BookingFailedException;
import com.example.aviasale.expection.FlightsNotFoundException;
import com.example.aviasale.expection.TicketFlightsNotFoundException;
import com.example.aviasale.expection.TicketsNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.util.*;

@Service
public class RegBookingService {
    private final BookingsService bookingsService;
    private final TicketFlightsService ticketFlightsService;
    private final TicketsService ticketsService;
    private final FlightsService flightsService;

    @Autowired
    public RegBookingService(BookingsService bookingsService, TicketFlightsService ticketFlightsService,
                             TicketsService ticketsService, FlightsService flightsService) {
        this.bookingsService = bookingsService;

        this.ticketFlightsService = ticketFlightsService;
        this.ticketsService = ticketsService;
        this.flightsService = flightsService;
    }

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public void prepareBooking(User user, List<PassengersDto> passengersDto, SearchFormDto searchFormDto, List<Price> prices) throws BookingFailedException, FlightsNotFoundException, TicketFlightsNotFoundException {
        if (prices.size() == 1) {
            SHESHA(user, passengersDto, searchFormDto, prices);
        } else if (prices.size() == 2) {
            SHESHA(user, passengersDto, searchFormDto, prices);
        } else {
            throw new BookingFailedException("SOMETHING WRONG", HttpStatus.BAD_REQUEST);
        }
    }

    public void SHESHA(User user, List<PassengersDto> passengersDto, SearchFormDto searchFormDto, List<Price> prices) throws BookingFailedException, FlightsNotFoundException, TicketFlightsNotFoundException {
        Bookings booking = bookingsService.createBooking(user, prices, searchFormDto.getNumberOfTickets());
        for (Price price : prices) {
            Flights flight = flightsService.getFlightByFlightId(price.getFlightId());
            Integer freeSeats = ticketFlightsService.getFreeSeats(flight.getFlightId(), flight.getAircraft(), searchFormDto.getConditions());
            if (freeSeats <= 0) {
                throw new BookingFailedException("No available Seats in Flight" + flight.getFlightId(), HttpStatus.UPGRADE_REQUIRED);
            }
            List<Tickets> tickets = ticketsService.createTickets(booking, passengersDto);
            ticketFlightsService.createTicketsFlights(tickets, price, searchFormDto);
        }
    }


    @Transactional
    public Bookings deleteBookingRelations(String bookRef) throws TicketsNotFoundException, TicketFlightsNotFoundException {
        Bookings booking = bookingsService.getBookingByBookRef(bookRef);
        List<Tickets> ticketsList = ticketsService.getTicketsByBookRef(booking.getBookRef());
        for (Tickets ticket : ticketsList) {
            this.removePassenger(ticket.getTicketNumber());
        }
        return booking;
    }



    @Transactional
    public void removePassenger(String ticketNo) throws TicketFlightsNotFoundException {
        TicketFlights ticketFlights = ticketFlightsService.getTicketFlightsByTicketNo(ticketNo);
        ticketFlightsService.deleteTicketFlights(ticketFlights);
        ticketsService.deleteTicketByTicketNo(ticketNo);
    }

    public List<BookingsDto> getAllBookingDtoByUser(User user) throws TicketsNotFoundException {
        List<Bookings> bookings = bookingsService.getBookingsByUser(user);
        List<BookingsDto> dtoList = new ArrayList<>();
        for (Bookings b : bookings) {
            List<Tickets> tickets = ticketsService.getTicketsByBookRef(b.getBookRef());
            BookingsDto bookingsDto = new BookingsDto();
            bookingsDto.setBooking(b);
            bookingsDto.setTickets(tickets);
            dtoList.add(bookingsDto);
        }
        return dtoList;
    }
}


package com.example.aviasale.service;

import com.example.aviasale.domain.dto.apiDto.BookingsDto;
import com.example.aviasale.domain.dto.apiDto.PassengersDto;
import com.example.aviasale.domain.entity.*;
import com.example.aviasale.domain.pojo.Price;
import com.example.aviasale.expection.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.util.ArrayList;
import java.util.List;

@Service
public class RegBookingService {
    private final BookingsService bookingsService;
    private final TicketFlightsService ticketFlightsService;
    private final TicketsService ticketsService;
    private final FlightsService flightsService;
    private final SearchQueryService searchQuery;

    @Autowired
    public RegBookingService(BookingsService bookingsService, TicketFlightsService ticketFlightsService,
                             TicketsService ticketsService, FlightsService flightsService,
                             SearchQueryService searchQuery) {
        this.bookingsService = bookingsService;
        this.ticketFlightsService = ticketFlightsService;
        this.ticketsService = ticketsService;
        this.flightsService = flightsService;
        this.searchQuery = searchQuery;
    }

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public void prepareBooking(User user, List<PassengersDto> passengers) throws BookingFailedException, FlightsNotFoundException {
        List<Price> prices = searchQuery.getPrices();
        Bookings booking = bookingsService.createBooking(user, prices, searchQuery.getSearchQueryDto().getNumberOfTickets());
        String conditions = searchQuery.getSearchQueryDto().getConditions();
        for (Price price : prices) {
            Flights flight = flightsService.getFlightByFlightId(price.getFlightId());
            Integer freeSeats = ticketFlightsService.getFreeSeats(flight.getFlightId(), flight.getAircraft(), conditions);
            if (freeSeats <= 0) {
                throw new BookingFailedException("No free seats in flight = "
                        + flight.getFlightId(), HttpStatus.NOT_FOUND);
            }
            List<Tickets> tickets = ticketsService.createTickets(booking, passengers);
            ticketFlightsService.createTicketsFlights(tickets, price, conditions);
        }
    }

    @Transactional
    public Bookings deleteBookingRelations(String bookRef) throws TicketFlightsNotFoundException, BookingNotFoundException {
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

    public List<BookingsDto> getAllBookingDtoByUser(User user) {
        List<Bookings> bookings = bookingsService.getBookingsByUser(user);
        List<BookingsDto> bookingDtos = new ArrayList<>();
        for (Bookings booking : bookings) {
            List<Tickets> tickets = ticketsService.getTicketsByBookRef(booking.getBookRef());
            BookingsDto bookingsDto = new BookingsDto();
            bookingsDto.setBooking(booking);
            bookingsDto.setTickets(tickets);
            bookingDtos.add(bookingsDto);
        }
        return bookingDtos;
    }
}


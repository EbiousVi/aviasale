package com.example.aviasale.service;

import com.example.aviasale.domain.pojo.Price;
import com.example.aviasale.domain.dto.apiDto.BookingsDto;
import com.example.aviasale.domain.dto.apiDto.PassengersData;
import com.example.aviasale.domain.dto.apiDto.SearchFormDto;
import com.example.aviasale.domain.entity.*;
import com.example.aviasale.domain.json.ContactsJson;
import com.example.aviasale.expection.BookingFailedException;
import com.example.aviasale.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class BookingService {
    private final TicketsRepository ticketsRepository;
    private final TicketFlightsRepository ticketFlightsRepository;
    private final BookingsRepository bookingsRepository;
    private final FlightsRepository flightsRepository;

    public BookingService(TicketsRepository ticketsRepository, TicketFlightsRepository ticketFlightsRepository, BookingsRepository bookingsRepository, FlightsRepository flightsRepository) {
        this.ticketsRepository = ticketsRepository;
        this.ticketFlightsRepository = ticketFlightsRepository;
        this.bookingsRepository = bookingsRepository;
        this.flightsRepository = flightsRepository;
    }

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public void prepareBooking(User user, List<PassengersData> passengersDataList, SearchFormDto searchFormDto, Price price) throws BookingFailedException {
        Flights flight = flightsRepository.findByFlightId(price.getFlightId());
        Optional<Integer> availableSeats = ticketFlightsRepository.availableSeats(flight.getFlightId(), flight.getAircraft(), searchFormDto.getConditions());

        if (availableSeats.isPresent()) {
            if (searchFormDto.getNumberOfTickets() <= availableSeats.get()) {
                Bookings booking = createBooking(user, price, searchFormDto.getNumberOfTickets());
                List<Tickets> tickets = createTickets(booking, passengersDataList);
                createTicketsFlights(tickets, price, searchFormDto);
            }
        } else {
            throw new BookingFailedException("No available seats in this Flight!", HttpStatus.UPGRADE_REQUIRED);
        }
    }

    public Bookings createBooking(User user, Price price, Integer numberOfTickets) {
        Bookings booking = new Bookings();
        booking.setBookDate(LocalDateTime.now());
        booking.setBookRef(UUID.randomUUID().toString().substring(0, 6));
        booking.setTotalAmount(price.getValue().doubleValue() * numberOfTickets);
        booking.setUser(user);
        bookingsRepository.save(booking);

        System.err.println(1);
        System.err.println(booking);
        return booking;
    }

    public List<Tickets> createTickets(Bookings booking, List<PassengersData> passengersDataList) {
        List<Tickets> tickets = new ArrayList<>();
        for (PassengersData p : passengersDataList) {
            Tickets ticket = new Tickets();
            ticket.setTicketNumber(UUID.randomUUID().toString().substring(0, 13));
            ticket.setBookRef(booking.getBookRef());
            ticket.setPassengerId(p.getPassengerId());
            ticket.setPassengerName(p.getPassengerName());

            ObjectMapper objectMapper = new ObjectMapper();
            ContactsJson contactsJson = new ContactsJson();
            contactsJson.setEmail(p.getEmail());
            contactsJson.setPhone(p.getPhone());
            try {
                String contacts = objectMapper.writeValueAsString(contactsJson);
                ticket.setContactDataJsonb(contacts);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            ticketsRepository.save(ticket);
            tickets.add(ticket);
        }
        System.err.println(2);
        System.err.println(tickets);
        return tickets;
    }

    public void createTicketsFlights(List<Tickets> tickets, Price price, SearchFormDto searchFormDto) {
        for (Tickets t : tickets) {
            TicketFlights ticketFlight = new TicketFlights();
            TicketFlights.CompositeKey compositeKey = new TicketFlights.CompositeKey();
            compositeKey.setFlightId(price.getFlightId());
            compositeKey.setTicketNumber(t.getTicketNumber());
            ticketFlight.setCompositeKey(compositeKey);

            ticketFlight.setFareConditions(searchFormDto.getConditions());
            ticketFlight.setAmount(price.getValue().doubleValue());
            ticketFlightsRepository.save(ticketFlight);
            System.err.println(3);
            System.err.println(ticketFlight);
        }
    }

    @Transactional
    public Bookings deleteBookingRelations(String bookRef) {
        Bookings booking = this.getBookingByBookRef(bookRef);
        List<Tickets> ticketsList = ticketsRepository.findByBookRef(booking.getBookRef());
        for (Tickets t : ticketsList) {
            TicketFlights ticketFlights = ticketFlightsRepository.findByTicketNumber(t.getTicketNumber());
            ticketFlightsRepository.delete(ticketFlights);
            ticketsRepository.delete(t);
        }
        return booking;
    }

    @Transactional
    public void deleteBooking(Bookings booking) {
        bookingsRepository.deleteByBookRef(booking.getBookRef());
    }

    public Bookings getBookingByBookRef(String bookRef) {
        return bookingsRepository.findByBookRef(bookRef);
    }

    public List<BookingsDto> getAllBookingDtoByUser(User user) {
        List<Bookings> bookings = bookingsRepository.findAllByUser(user);
        List<BookingsDto> dtoList = new ArrayList<>();
        for (Bookings b : bookings) {
            List<Tickets> tickets = ticketsRepository.findByBookRef(b.getBookRef());
            BookingsDto bookingsDto = new BookingsDto();
            bookingsDto.setBooking(b);
            bookingsDto.setTickets(tickets);
            bookingsDto.setShow(true);
            dtoList.add(bookingsDto);
        }
        return dtoList;
    }

    @Transactional
    public void removePassenger(String ticketNumber) {
        TicketFlights ticketFlights = ticketFlightsRepository.findByTicketNumber(ticketNumber);
        ticketFlightsRepository.delete(ticketFlights);
        ticketsRepository.deleteByTicketNumber(ticketNumber);
    }
}


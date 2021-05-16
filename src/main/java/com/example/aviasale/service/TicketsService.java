package com.example.aviasale.service;

import com.example.aviasale.domain.dto.apiDto.PassengersDto;
import com.example.aviasale.domain.entity.Bookings;
import com.example.aviasale.domain.entity.Tickets;
import com.example.aviasale.domain.json.ContactsJson;
import com.example.aviasale.expection.TicketsNotFoundException;
import com.example.aviasale.repository.TicketsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TicketsService {

    private final TicketsRepository ticketsRepository;

    @Autowired
    public TicketsService(TicketsRepository ticketsRepository) {
        this.ticketsRepository = ticketsRepository;
    }

    public List<Tickets> createTickets(Bookings booking, List<PassengersDto> passengers) {
        List<Tickets> tickets = new ArrayList<>();
        for (PassengersDto p : passengers) {
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

            }
            ticketsRepository.save(ticket);
            tickets.add(ticket);
        }
        System.err.println(2);
        System.err.println(tickets);
        return tickets;
    }

    public void deleteTicketByTicketNo(String ticketNo) {
        ticketsRepository.deleteByTicketNumber(ticketNo);
    }

    public List<Tickets> getTicketsByBookRef(String bookRef) throws TicketsNotFoundException {
        return ticketsRepository.findByBookRef(bookRef)
                .orElseThrow(() -> new TicketsNotFoundException("Tickets not Found by BookRef", HttpStatus.NOT_FOUND));
    }


}

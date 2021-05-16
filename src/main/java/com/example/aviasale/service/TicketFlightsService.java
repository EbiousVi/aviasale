package com.example.aviasale.service;

import com.example.aviasale.domain.dto.apiDto.SearchQueryDto;
import com.example.aviasale.domain.entity.TicketFlights;
import com.example.aviasale.domain.entity.Tickets;
import com.example.aviasale.domain.pojo.Price;
import com.example.aviasale.expection.TicketFlightsNotFoundException;
import com.example.aviasale.repository.TicketFlightsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketFlightsService {

    private final TicketFlightsRepository ticketFlightsRepository;

    @Autowired
    public TicketFlightsService(TicketFlightsRepository ticketFlightsRepository) {
        this.ticketFlightsRepository = ticketFlightsRepository;
    }

    public Integer getFreeSeats(Integer flightId, String aircraft, String conditions) {
        Optional<Integer> freeSeats = ticketFlightsRepository.freeSeats(flightId, aircraft, conditions);
        return freeSeats.orElse(0);
    }

    public void createTicketsFlights(List<Tickets> tickets, Price price, SearchQueryDto searchQueryDto) {
        for (Tickets ticket : tickets) {
            TicketFlights ticketFlight = new TicketFlights();
            TicketFlights.CompositeKey compositeKey = new TicketFlights.CompositeKey();
            compositeKey.setFlightId(price.getFlightId());
            compositeKey.setTicketNumber(ticket.getTicketNumber());
            ticketFlight.setCompositeKey(compositeKey);
            ticketFlight.setFareConditions(searchQueryDto.getConditions());
            ticketFlight.setAmount(price.getValue().doubleValue());
            ticketFlightsRepository.save(ticketFlight);
            System.err.println(3);
            System.err.println(ticketFlight);
        }
    }

    public TicketFlights getTicketFlightsByTicketNo(String ticketNo) throws TicketFlightsNotFoundException {
        return ticketFlightsRepository.findByTicketNo(ticketNo)
                .orElseThrow(() -> new TicketFlightsNotFoundException("TicketFlights not found by TicketNo" + ticketNo, HttpStatus.NOT_FOUND));
    }

    public void deleteTicketFlights(TicketFlights ticketFlights) {
        ticketFlightsRepository.delete(ticketFlights);
    }
}

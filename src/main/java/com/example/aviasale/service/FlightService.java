package com.example.aviasale.service;

import com.example.aviasale.domain.pojo.Price;
import com.example.aviasale.domain.dto.apiDto.SearchFormDto;
import com.example.aviasale.domain.dto.apiDto.FlightsDto;
import com.example.aviasale.domain.entity.Flights;
import com.example.aviasale.repository.FlightsRepository;
import com.example.aviasale.repository.TicketFlightsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FlightService {
    final private FlightsRepository flightsRepository;
    final private AirportsService airportsService;
    final private TicketFlightsRepository ticketFlightsRepository;
    private SearchFormDto searchFormDto;

    @Autowired
    public FlightService(FlightsRepository flightsRepository, AirportsService airportsService, TicketFlightsRepository ticketFlightsRepository) {
        this.flightsRepository = flightsRepository;
        this.airportsService = airportsService;
        this.ticketFlightsRepository = ticketFlightsRepository;
    }

    public void setSearchFormDto(SearchFormDto searchFormDto) {
        this.searchFormDto = searchFormDto;
    }

    private List<FlightsDto> prepareResultDto(List<Flights> flights, Boolean interval) {
        List<FlightsDto> list = new ArrayList<>();
        for (Flights flight : flights) {
            FlightsDto flightsDto = new FlightsDto();
            flightsDto.setShow(false);
            flightsDto.setInterval(interval);
            flightsDto.setFlight(flight);
            flightsDto.setAirportFrom(airportsService.getAirportFrom(flight.getFlightId()));
            flightsDto.setAirportTo(airportsService.getAirportTo(flight.getFlightId()));
            Price price = new Price();
            price.setFlightId(flight.getFlightId());
            price.setValue(BigDecimal.valueOf(calculatePrice(searchFormDto.getConditions())));
            flightsDto.setPrice(price);
            list.add(flightsDto);
        }
        return list;
    }

    public List<FlightsDto> getFlights() {
        List<Flights> flights = filter(this.findFlights());
        return prepareResultDto(flights, false);
    }

    public List<FlightsDto> getFlightsInterval() {
        List<Flights> flights = filter(this.findFlightsInterval());
        return prepareResultDto(flights, true);
    }

    public List<Flights> findFlights() {
        List<String> airportsInCityFrom = airportsService.getAllAirportsInCity(searchFormDto.getCityFrom());
        List<String> airportsInCityTo = airportsService.getAllAirportsInCity(searchFormDto.getCityTo());

        LocalDateTime date1 = LocalDateTime.parse(searchFormDto.getDate() + "T00:00:00");
        LocalDateTime date2 = LocalDateTime.parse(searchFormDto.getDate() + "T23:59:00");
        return flightsRepository.findAllFlightsByParam(airportsInCityFrom, airportsInCityTo, date1, date2);
    }

    public List<Flights> findFlightsInterval() {
        List<String> airportsInCityFrom = airportsService.getAllAirportsInCity(searchFormDto.getCityFrom());
        List<String> airportsInCityTo = airportsService.getAllAirportsInCity(searchFormDto.getCityTo());

        LocalDateTime minusWeek = LocalDateTime.parse(searchFormDto.getDate() + "T00:00:00").minusWeeks(1);
        LocalDateTime plusWeek = LocalDateTime.parse(searchFormDto.getDate() + "T23:59:00").plusWeeks(1);
        return flightsRepository.findAllFlightsByParam(airportsInCityFrom, airportsInCityTo, minusWeek, plusWeek);
    }

    /**
     * Check available seats. If number of tickets from request less or equal number of available seats
     * then flight fits the condition.
     */
    @Transactional
    public List<Flights> filter(List<Flights> flights) {
        return flights.stream().filter(fl -> {
            Optional<Integer> availableSeats = ticketFlightsRepository.availableSeats(fl.getFlightId(),
                    fl.getAircraft(), searchFormDto.getConditions());
            return searchFormDto.getNumberOfTickets() <= availableSeats
                    .orElse(ticketFlightsRepository.allSeats(fl.getAircraft(), searchFormDto.getConditions()).orElse(0));
        }).collect(Collectors.toList());
    }

    public Double calculatePrice(String fareConditions) {
        if (fareConditions.equalsIgnoreCase("economy")) {
            return 5000D + Math.round(Math.random() * 5000);
        } else if (fareConditions.equalsIgnoreCase("comfort")) {
            return 12000D + Math.round(Math.random() * 10000);
        } else if (fareConditions.equalsIgnoreCase("business")) {
            return 23000D + Math.round(Math.random() * 15000);
        } else {
            return 0D;
        }
    }
}

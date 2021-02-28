package com.example.aviasale.service;

import com.example.aviasale.domain.dto.apiDto.FlightDto;
import com.example.aviasale.domain.dto.apiDto.SearchFormDto;
import com.example.aviasale.domain.entity.Flights;
import com.example.aviasale.domain.pojo.Price;
import com.example.aviasale.repository.FlightsRepository;
import com.example.aviasale.repository.TicketFlightsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FlightService {
    final private FlightsRepository flightsRepository;
    final private AirportsService airportsService;
    final private TicketFlightsRepository ticketFlightsRepository;

    final private ConnFlightService connFlightService;

    private SearchFormDto searchFormDto;

    @Autowired
    public FlightService(FlightsRepository flightsRepository, AirportsService airportsService,
                         TicketFlightsRepository ticketFlightsRepository, @Lazy ConnFlightService connFlightService) {
        this.flightsRepository = flightsRepository;
        this.airportsService = airportsService;
        this.ticketFlightsRepository = ticketFlightsRepository;
        this.connFlightService = connFlightService;
    }

    public void setSearchFormDto(SearchFormDto searchFormDto) {
        this.searchFormDto = searchFormDto;
    }

    public LocalDateTime dayStart() {
        return LocalDateTime.parse(searchFormDto.getDate() + "T00:00:00");
    }

    public LocalDateTime dayEnd() {
        return LocalDateTime.parse(searchFormDto.getDate() + "T23:59:00");
    }

    private List<FlightDto> prepareDto(List<Flights> flights, Boolean interval) {
        List<FlightDto> list = new ArrayList<>();
        for (Flights flight : flights) {
            FlightDto flightDto = new FlightDto();
            flightDto.setShow(false);
            flightDto.setInterval(interval);
            flightDto.setFlight(flight);
            flightDto.setAirportFrom(airportsService.getAirportFrom(flight.getFlightId()));
            flightDto.setAirportTo(airportsService.getAirportTo(flight.getFlightId()));
            Price price = new Price();
            price.setFlightId(flight.getFlightId());
            price.setValue(calculatePrice(searchFormDto.getConditions()));
            flightDto.setPrice(price);
            list.add(flightDto);
        }
        return list;
    }

    public ResponseEntity<?> getFlights() {
        List<Flights> flights = filter(this.findFlights());
        if (flights.size() == 0) {
            List<Flights> flightsInterval = this.getFlightsInterval();
            if (flightsInterval.size() == 0) {
                connFlightService.setSearchFormDto(searchFormDto);
                return new ResponseEntity<>(connFlightService.prepareDto(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(prepareDto(flightsInterval, true), HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>(prepareDto(flights, false), HttpStatus.OK);
        }
    }

    public List<Flights> getFlightsInterval() {
        return filter(this.findFlightsInterval());
    }

    public List<Flights> findFlights() {
        List<String> airportsInCityFrom = airportsService.getAllAirportsInCity(searchFormDto.getCityFrom());
        List<String> airportsInCityTo = airportsService.getAllAirportsInCity(searchFormDto.getCityTo());

        List<Flights> flights = flightsRepository.findAllFlightsByParam(airportsInCityFrom, airportsInCityTo, dayStart(), dayEnd());
        if (flights.size() == 0) {
            return flightsRepository.findAllFlightsByParam(airportsInCityFrom, airportsInCityTo, dayStart().minusDays(4), dayEnd().plusDays(4));
        }
        return flights;
    }

    public List<Flights> findFlightsInterval() {
        List<String> airportsInCityFrom = airportsService.getAllAirportsInCity(searchFormDto.getCityFrom());
        List<String> airportsInCityTo = airportsService.getAllAirportsInCity(searchFormDto.getCityTo());

        LocalDateTime minusWeek = LocalDateTime.parse(searchFormDto.getDate() + "T00:00:00").minusDays(5);
        LocalDateTime plusWeek = LocalDateTime.parse(searchFormDto.getDate() + "T23:59:00").plusDays(5);
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

    public BigDecimal calculatePrice(String fareConditions) {
        if (fareConditions.equalsIgnoreCase("economy")) {
            return BigDecimal.valueOf(5000D + Math.round(Math.random() * 5000));
        } else if (fareConditions.equalsIgnoreCase("comfort")) {
            return BigDecimal.valueOf(12000D + Math.round(Math.random() * 10000));
        } else if (fareConditions.equalsIgnoreCase("business")) {
            return BigDecimal.valueOf(23000D + Math.round(Math.random() * 15000));
        }
        return BigDecimal.valueOf(Integer.MAX_VALUE);
    }

    // lat = широта; lng = долгота;
    public double distByCoordinates(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return (earthRadius * c);
    }
}

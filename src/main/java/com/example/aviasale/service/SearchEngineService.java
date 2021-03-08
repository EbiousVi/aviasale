package com.example.aviasale.service;

import com.example.aviasale.domain.dto.apiDto.ConnFlightDto;
import com.example.aviasale.domain.dto.apiDto.OneWayFlightDto;
import com.example.aviasale.domain.dto.apiDto.SearchFormDto;
import com.example.aviasale.domain.entity.Airports;
import com.example.aviasale.domain.entity.Flights;
import com.example.aviasale.expection.AirportsNotFoundException;
import com.example.aviasale.expection.FlightsNotFoundException;
import com.example.aviasale.expection.TicketFlightsNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchEngineService {

    private final FlightsService flightsService;
    final private AirportsService airportsService;
    final private TicketFlightsService ticketFlightsService;
    final private PriceService priceService;
    private SearchFormDto searchFormDto;

    @Autowired
    public SearchEngineService(FlightsService flightsService, AirportsService airportsService,
                               TicketFlightsService ticketFlightsService, PriceService priceService) {
        this.flightsService = flightsService;

        this.airportsService = airportsService;
        this.ticketFlightsService = ticketFlightsService;

        this.priceService = priceService;
    }

    public void setSearchFormDto(SearchFormDto searchFormDto) {
        this.searchFormDto = searchFormDto;
    }

    public LocalDateTime searchDateStart() {
        return LocalDateTime.parse(searchFormDto.getDate() + "T00:00:00");
    }

    public LocalDateTime searchDateEnd() {
        return LocalDateTime.parse(searchFormDto.getDate() + "T23:59:59");
    }

    public ResponseEntity<?> getResult() throws AirportsNotFoundException, FlightsNotFoundException, TicketFlightsNotFoundException {
        List<Flights> flights = this.getFlights();
        if (flights.size() == 0) {
            List<Flights> flightsInterval = this.getFlightsInterval();
            if (flightsInterval.size() == 0) {
                Map<String, Flights> connFlight = this.getConnFlight();
                if (connFlight.size() > 0) {
                    return new ResponseEntity<>(prepareConnFlight(connFlight), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("Nothing found by selected params", HttpStatus.OK);
                }
            } else {
                return new ResponseEntity<>(prepareOneWayFlight(flightsInterval, true), HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>(prepareOneWayFlight(flights, false), HttpStatus.OK);
        }
    }

    private List<OneWayFlightDto> prepareOneWayFlight(List<Flights> flights, Boolean interval) throws AirportsNotFoundException {
        List<OneWayFlightDto> list = new ArrayList<>();
        for (Flights flight : flights) {
            OneWayFlightDto oneWayFlightDto = new OneWayFlightDto();
            oneWayFlightDto.setInterval(interval);
            oneWayFlightDto.setFlight(flight);
            oneWayFlightDto.setAirportFrom(airportsService.getAirportFrom(flight.getFlightId()));
            oneWayFlightDto.setAirportTo(airportsService.getAirportTo(flight.getFlightId()));
            oneWayFlightDto.setPrice(priceService.createPrice(flight.getFlightId(), flight.getFlightNo(), searchFormDto.getConditions()));
            list.add(oneWayFlightDto);
        }
        return list;
    }

    public List<Flights> getFlightsInterval() throws AirportsNotFoundException, FlightsNotFoundException {
        return this.filter(this.findFlightsInterval());
    }

    public List<Flights> getFlights() throws AirportsNotFoundException, FlightsNotFoundException {
        return this.filter(this.findFlights());
    }

    /**
     * Check available seats. If number of tickets from request less or equal number of available seats
     * then flight fits the condition.
     */
    @Transactional
    public List<Flights> filter(List<Flights> flights) {
        return flights.stream().filter(fl -> {
            Integer availableSeats = ticketFlightsService.getFreeSeats(fl.getFlightId(), fl.getAircraft(), searchFormDto.getConditions());
            if (availableSeats > 0) {
                return searchFormDto.getNumberOfTickets() <= availableSeats;
            } else {
                return false;
            }
        }).collect(Collectors.toList());
    }

    public List<Flights> findFlights() throws AirportsNotFoundException, FlightsNotFoundException {
        List<String> airportsInCityFrom = airportsService.getAllAirportsInCity(searchFormDto.getCityFrom());
        List<String> airportsInCityTo = airportsService.getAllAirportsInCity(searchFormDto.getCityTo());
        return flightsService.getFlightsByParam(airportsInCityFrom, airportsInCityTo, searchDateStart(), searchDateEnd());
    }

    public List<Flights> findFlightsInterval() throws AirportsNotFoundException, FlightsNotFoundException {
        List<String> airportsInCityFrom = airportsService.getAllAirportsInCity(searchFormDto.getCityFrom());
        List<String> airportsInCityTo = airportsService.getAllAirportsInCity(searchFormDto.getCityTo());
        LocalDateTime date1 = searchDateStart().minusDays(4);
        LocalDateTime date2 = searchDateEnd().plusDays(4);
        return flightsService.getFlightsByParam(airportsInCityFrom, airportsInCityTo, date1, date2);
    }

    public List<ConnFlightDto> prepareConnFlight(Map<String, Flights> map) throws AirportsNotFoundException {
        List<ConnFlightDto> dtoList = new ArrayList<>();
        List<Flights> values = new ArrayList<>(map.values());
        for (int i = 0; i < values.size(); i += 2) {
            ConnFlightDto dto = new ConnFlightDto();
            dto.setFlight1(values.get(i));
            dto.setFlight2(values.get(i + 1));
            dto.setPrice1(priceService.createPrice(values.get(i).getFlightId(), values.get(i).getFlightNo(), searchFormDto.getConditions()));
            dto.setPrice2(priceService.createPrice(values.get(i + 1).getFlightId(), values.get(i + 1).getFlightNo(), searchFormDto.getConditions()));
            Airports airFrom1 = airportsService.getAirportFrom(values.get(i).getFlightId());
            Airports airTo1 = airportsService.getAirportTo(values.get(i).getFlightId());
            dto.setAirFrom1(airFrom1);
            dto.setAirTo1(airTo1);
            Airports airFrom2 = airportsService.getAirportFrom(values.get(i + 1).getFlightId());
            Airports airTo2 = airportsService.getAirportTo(values.get(i + 1).getFlightId());
            dto.setAirFrom2(airFrom2);
            dto.setAirTo2(airTo2);
            dto.setConn(true);
            dtoList.add(dto);
        }
        //Sort by duration
        dtoList.sort((o1, o2) -> {
            Duration duration1 = Duration.between(o1.getFlight1().getDepartureDate(), o1.getFlight2().getArrivalDate());
            Duration duration2 = Duration.between(o2.getFlight1().getDepartureDate(), o2.getFlight2().getArrivalDate());
            return Long.compare(duration1.toHours(), duration2.toHours());
        });
        return dtoList;
    }

    public Map<String, Flights> getConnFlight() throws AirportsNotFoundException, FlightsNotFoundException, TicketFlightsNotFoundException {
        return this.connectingFlights();
    }

    public Map<String, Flights> connectingFlights() throws AirportsNotFoundException, FlightsNotFoundException, TicketFlightsNotFoundException {
        List<String> airportsInCityFrom = airportsService.getAllAirportsInCity(searchFormDto.getCityFrom());
        List<String> airportsInCityTo = airportsService.getAllAirportsInCity(searchFormDto.getCityTo());
        List<String> intersect = flightsService.getIntersectArrivalAirports(airportsInCityFrom, airportsInCityTo);
        if (intersect.size() > 0) {
            List<String> MOSCOW = Arrays.asList("SVO", "DME", "VKO");
            List<String> MOSCOWFirst = intersect.stream()
                    .filter(MOSCOW::contains)
                    .collect(Collectors.toList());
            if (MOSCOWFirst.size() > 0) {
                Map<String, Flights> mscMap = connFlightsFilter(MOSCOWFirst, airportsInCityFrom, airportsInCityTo);
                intersect.removeAll(MOSCOWFirst);
                Map<String, Flights> other = connFlightsFilter(intersect, airportsInCityFrom, airportsInCityTo);
                mscMap.putAll(other);
                return mscMap;
            }
            return connFlightsFilter(intersect, airportsInCityFrom, airportsInCityTo);
        }
        return Collections.emptyMap();
    }

    public Map<String, Flights> connFlightsFilter(List<String> airports, List<String> airFrom, List<String> airTo) throws FlightsNotFoundException, TicketFlightsNotFoundException {
        List<Flights> first = flightsService.getFlightsByParam(airFrom, airports, searchDateStart().minusDays(1), searchDateEnd().plusDays(1));
        List<Flights> second = flightsService.getFlightsByParam(airports, airTo, searchDateStart().minusDays(1), searchDateEnd().plusDays(1));
        if (!first.isEmpty() && !second.isEmpty()) {
            Map<String, Flights> map = new LinkedHashMap<>();
            long prevDiff = 24;
            for (Flights f : first) {
                for (Flights s : second) {
                    Duration duration = Duration.between(f.getArrivalDate(), s.getDepartureDate());
                    long diff = duration.toHours();
                    if (diff >= 1 && diff < 24 && diff <= prevDiff) {
                        System.err.println("duration = " + duration.toHours());
                        Integer availableSeats = ticketFlightsService.getFreeSeats(f.getFlightId(), f.getAircraft(), searchFormDto.getConditions());
                        if (searchFormDto.getNumberOfTickets() <= availableSeats) {
                            String key = "|" + UUID.randomUUID().toString().substring(0, 4);
                            map.put(diff + "_F" + key, f);
                            map.put(diff + "_S" + key, s);
                            prevDiff = diff;
                        }
                    }
                }
            }
            if (map.size() > 0) return map;
        }
        return Collections.emptyMap();
    }
}

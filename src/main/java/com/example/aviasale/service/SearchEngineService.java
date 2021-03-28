package com.example.aviasale.service;

import com.example.aviasale.domain.dto.apiDto.ConnFlightDto;
import com.example.aviasale.domain.dto.apiDto.OneWayFlightDto;
import com.example.aviasale.domain.dto.apiDto.SearchFormDto;
import com.example.aviasale.domain.entity.Airports;
import com.example.aviasale.domain.entity.Flights;
import com.example.aviasale.expection.AirportsNotFoundException;
import com.example.aviasale.expection.FlightsNotFoundException;
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
    private final AirportsService airportsService;
    private final TicketFlightsService ticketFlightsService;
    private final PriceService priceService;
    private SearchFormDto searchFormDto;
    private final List<String> MOSCOW_AIRPORTS = Arrays.asList("SVO", "DME", "VKO");

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

    public ResponseEntity<?> getFlights() throws AirportsNotFoundException, FlightsNotFoundException {
        List<Flights> oneWayFlights = this.getOneWayFlights();
        if (oneWayFlights.size() == 0) {
            List<Flights> oneWayFlightsInterval = this.getOneWayFlightsInterval();
            if (oneWayFlightsInterval.size() == 0) {
                Map<String, Flights> connFlights = this.getConnFlights();
                if (connFlights.size() > 0) {
                    return new ResponseEntity<>(prepareConnFlights(connFlights), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("Nothing found by selected params", HttpStatus.OK);
                }
            } else {
                return new ResponseEntity<>(prepareOneWayFlights(oneWayFlightsInterval, true), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(prepareOneWayFlights(oneWayFlights, false), HttpStatus.OK);
    }

    private List<OneWayFlightDto> prepareOneWayFlights(List<Flights> flights, Boolean interval) throws AirportsNotFoundException {
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

    public List<Flights> getOneWayFlightsInterval() throws AirportsNotFoundException, FlightsNotFoundException {
        return this.oneWayFlightsFilter(this.findOneWayFlightsInterval());
    }

    public List<Flights> getOneWayFlights() throws AirportsNotFoundException, FlightsNotFoundException {
        return this.oneWayFlightsFilter(this.findOneWayFlights());
    }

    /**
     * Check available seats. If number of tickets from request less or equal number of available seats
     * then flight fits the condition.
     */
    @Transactional
    public List<Flights> oneWayFlightsFilter(List<Flights> flights) {
        return flights.stream().filter(fl -> {
            Integer availableSeats = ticketFlightsService.getFreeSeats(fl.getFlightId(), fl.getAircraft(), searchFormDto.getConditions());
            if (availableSeats > 0) {
                return searchFormDto.getNumberOfTickets() <= availableSeats;
            } else {
                return false;
            }
        }).collect(Collectors.toList());
    }

    public List<Flights> findOneWayFlights() throws AirportsNotFoundException, FlightsNotFoundException {
        List<String> airportsInCityFrom = airportsService.getAllAirportsInCity(searchFormDto.getCityFrom());
        List<String> airportsInCityTo = airportsService.getAllAirportsInCity(searchFormDto.getCityTo());
        return flightsService.getFlightsByParam(airportsInCityFrom, airportsInCityTo, searchDateStart(), searchDateEnd());
    }

    public List<Flights> findOneWayFlightsInterval() throws AirportsNotFoundException, FlightsNotFoundException {
        List<String> airportsInCityFrom = airportsService.getAllAirportsInCity(searchFormDto.getCityFrom());
        List<String> airportsInCityTo = airportsService.getAllAirportsInCity(searchFormDto.getCityTo());
        LocalDateTime date1 = searchDateStart().minusDays(4);
        LocalDateTime date2 = searchDateEnd().plusDays(4);
        return flightsService.getFlightsByParam(airportsInCityFrom, airportsInCityTo, date1, date2);
    }

    public List<ConnFlightDto> prepareConnFlights(Map<String, Flights> connFlights) throws AirportsNotFoundException {
        List<ConnFlightDto> dtoList = new ArrayList<>();
        List<Flights> connFlight = new ArrayList<>(connFlights.values());
        for (int i = 0; i < connFlight.size(); i += 2) {
            ConnFlightDto dto = new ConnFlightDto();
            dto.setFlight1(connFlight.get(i));
            dto.setFlight2(connFlight.get(i + 1));
            dto.setPrice1(priceService.createPrice(connFlight.get(i).getFlightId(), connFlight.get(i).getFlightNo(), searchFormDto.getConditions()));
            dto.setPrice2(priceService.createPrice(connFlight.get(i + 1).getFlightId(), connFlight.get(i + 1).getFlightNo(), searchFormDto.getConditions()));
            Airports airFrom1 = airportsService.getAirportFrom(connFlight.get(i).getFlightId());
            Airports airTo1 = airportsService.getAirportTo(connFlight.get(i).getFlightId());
            dto.setAirFrom1(airFrom1);
            dto.setAirTo1(airTo1);
            Airports airFrom2 = airportsService.getAirportFrom(connFlight.get(i + 1).getFlightId());
            Airports airTo2 = airportsService.getAirportTo(connFlight.get(i + 1).getFlightId());
            dto.setAirFrom2(airFrom2);
            dto.setAirTo2(airTo2);
            dto.setConn(true);
            dtoList.add(dto);
        }
        dtoList.sort(connFlightsComparatorByDuration());
        return dtoList;
    }

    public Comparator<ConnFlightDto> connFlightsComparatorByDuration() {
        return (o1, o2) -> {
            Duration duration1 = Duration.between(o1.getFlight1().getDepartureDate(), o1.getFlight2().getArrivalDate());
            Duration duration2 = Duration.between(o2.getFlight1().getDepartureDate(), o2.getFlight2().getArrivalDate());
            return Long.compare(duration1.toHours(), duration2.toHours());
        };
    }

    public Map<String, Flights> getConnFlights() throws AirportsNotFoundException, FlightsNotFoundException {
        return this.findConnFlights();
    }

    public Map<String, Flights> findConnFlights() throws AirportsNotFoundException, FlightsNotFoundException {
        List<String> airportsInCityFrom = airportsService.getAllAirportsInCity(searchFormDto.getCityFrom());
        List<String> airportsInCityTo = airportsService.getAllAirportsInCity(searchFormDto.getCityTo());
        List<String> intersectArrivalAirports = flightsService.getIntersectArrivalAirports(airportsInCityFrom, airportsInCityTo);
        if (intersectArrivalAirports.size() > 0) {
            List<String> MOSCOW_first = intersectArrivalAirports.stream()
                    .filter(MOSCOW_AIRPORTS::contains)
                    .collect(Collectors.toList());
            if (MOSCOW_first.size() > 0) {
                Map<String, Flights> transferInMoscowFlights = connFlightsFilter(MOSCOW_first, airportsInCityFrom, airportsInCityTo);
                intersectArrivalAirports.removeAll(MOSCOW_first);
                Map<String, Flights> transferInOtherCitiesFlights = connFlightsFilter(intersectArrivalAirports, airportsInCityFrom, airportsInCityTo);
                transferInMoscowFlights.putAll(transferInOtherCitiesFlights);
                return transferInMoscowFlights;
            }
            return connFlightsFilter(intersectArrivalAirports, airportsInCityFrom, airportsInCityTo);
        }
        return Collections.emptyMap();
    }

    public Map<String, Flights> connFlightsFilter(List<String> intersectArrivAirp, List<String> airFrom, List<String> airTo) throws FlightsNotFoundException {
        List<Flights> first = flightsService.getFlightsByParam(airFrom, intersectArrivAirp, searchDateStart(), searchDateEnd());
        List<Flights> second = flightsService.getFlightsByParam(intersectArrivAirp, airTo, searchDateStart(), searchDateEnd().plusDays(1));
        if (!first.isEmpty() && !second.isEmpty()) {
            Map<String, Flights> connFlights = new LinkedHashMap<>();
            long prevDiff = 24;
            for (Flights f : first) {
                for (Flights s : second) {
                    Duration duration = Duration.between(f.getArrivalDate(), s.getDepartureDate());
                    long dur = duration.toHours();
                    if (dur >= 1 && dur < 24 && dur <= prevDiff) {
                        Integer availableSeats = ticketFlightsService.getFreeSeats(f.getFlightId(), f.getAircraft(), searchFormDto.getConditions());
                        if (searchFormDto.getNumberOfTickets() <= availableSeats) {
                            String key = "|" + UUID.randomUUID().toString().substring(0, 4);
                            connFlights.put(dur + "_F_" + f.getFlightId() + "_" + key, f);
                            connFlights.put(dur + "_S_" + s.getFlightId() + "_" + key, s);
                            prevDiff = dur;
                        }
                    }
                }
            }
            if (connFlights.size() > 0) return connFlights;
        }
        return Collections.emptyMap();
    }
}

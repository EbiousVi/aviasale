package com.example.aviasale.service;

import com.example.aviasale.domain.dto.apiDto.FlightsDto;
import com.example.aviasale.domain.dto.apiDto.SearchFormDto;
import com.example.aviasale.domain.entity.Airports;
import com.example.aviasale.domain.entity.Flights;
import com.example.aviasale.domain.pojo.Price;
import com.example.aviasale.repository.FlightsRepository;
import com.example.aviasale.repository.TicketFlightsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ConnFlightService {
    final private FlightsRepository flightsRepository;
    final private AirportsService airportsService;
    final private TicketFlightsRepository ticketFlightsRepository;
    private SearchFormDto searchFormDto;

    @Autowired
    public ConnFlightService(FlightsRepository flightsRepository, AirportsService airportsService, TicketFlightsRepository ticketFlightsRepository, FlightService flightService) {
        this.flightsRepository = flightsRepository;
        this.airportsService = airportsService;
        this.ticketFlightsRepository = ticketFlightsRepository;
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

    public List<FlightsDto> prepareDto() {
        Map<String, Flights> map = this.newEngineFlight();
        List<FlightsDto> dtoList = new ArrayList<>();
        List<Flights> values = new ArrayList<>(map.values());
        for (int i = 0; i < values.size(); i += 2) {
            FlightsDto dto = new FlightsDto();
            dto.setFlight1(values.get(i));
            dto.setFlight2(values.get(i + 1));
            Price price1 = new Price();
            price1.setFlightId(values.get(i).getFlightId());
            price1.setValue(BigDecimal.valueOf(0D));
            dto.setPrice1(price1);
            Price price2 = new Price();
            price2.setFlightId(values.get(i + 1).getFlightId());
            price2.setValue(BigDecimal.valueOf(0D));
            dto.setPrice2(price2);
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
        return dtoList;
    }

    public Map<String, Flights> newEngineFlight() {
        List<String> airportsInCityFrom = airportsService.getAllAirportsInCity(searchFormDto.getCityFrom());
        List<String> airportsInCityTo = airportsService.getAllAirportsInCity(searchFormDto.getCityTo());
        List<String> intersect = flightsRepository.intersectArrivalAirports(airportsInCityFrom, airportsInCityTo);
        if (intersect.size() > 0) {
            List<String> MOSCOW = Arrays.asList("SVO", "DME", "VKO");
            List<String> MOSCOWFirst = intersect.stream()
                    .filter(MOSCOW::contains)
                    .collect(Collectors.toList());
            if (MOSCOWFirst.size() > 0) {
                Map<String, Flights> mscMap = fillMap(MOSCOWFirst, airportsInCityFrom, airportsInCityTo);
                intersect.removeAll(MOSCOWFirst);
                Map<String, Flights> other = fillMap(intersect, airportsInCityFrom, airportsInCityTo);
                mscMap.putAll(other);
                return mscMap;
            }
            return fillMap(intersect, airportsInCityFrom, airportsInCityTo);
        }
        return Collections.emptyMap();
    }

    public Map<String, Flights> fillMap(List<String> airports, List<String> airFrom, List<String> airTo) {
        List<Flights> first = flightsRepository.findAllFlightsByParam(airFrom, airports, dayStart().minusDays(1), dayEnd().plusDays(1));
        List<Flights> second = flightsRepository.findAllFlightsByParam(airports, airTo, dayStart().minusDays(1), dayEnd().plusDays(1));
        if (!first.isEmpty() && !second.isEmpty()) {
            Map<String, Flights> map = new TreeMap<>();
            long prevDiff = 24;
            for (Flights f : first) {
                for (Flights s : second) {
                    Duration duration = Duration.between(f.getArrivalDate(), s.getDepartureDate());
                    long diff = duration.toHours();
                    if (diff >= 1 && diff < 24 && diff < prevDiff) {
                        System.err.println("duration = " + duration.toHours());
                        if (searchFormDto.getNumberOfTickets() <= this.getAvailableSeats(f.getFlightId(), f.getAircraft())) {
                            String key = "dur=" + diff + "|" + UUID.randomUUID().toString().substring(0, 6);
                            map.put(key + "_F", f);
                            map.put(key + "_S", s);
                            prevDiff = diff;
                        }
                    }
                }
            }
            if (map.size() > 0) return map;
        }
        return Collections.emptyMap();
    }

    public Integer getAvailableSeats(Integer flightId, String aircraft) {
        Optional<Integer> availableSeats = ticketFlightsRepository.availableSeats(flightId, aircraft, searchFormDto.getConditions());
        return availableSeats
                .orElse(ticketFlightsRepository.allSeats(aircraft, searchFormDto.getConditions())
                        .orElse(0));
    }
}


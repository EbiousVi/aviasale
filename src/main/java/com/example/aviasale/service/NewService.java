package com.example.aviasale.service;

import com.example.aviasale.domain.dto.apiDto.SearchFormDto;
import com.example.aviasale.domain.entity.Flights;
import com.example.aviasale.repository.FlightsRepository;
import com.example.aviasale.repository.TicketFlightsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class NewService {
    final private FlightsRepository flightsRepository;
    final private AirportsService airportsService;
    final private TicketFlightsRepository ticketFlightsRepository;
    private SearchFormDto searchFormDto;

    @Autowired
    public NewService(FlightsRepository flightsRepository, AirportsService airportsService, TicketFlightsRepository ticketFlightsRepository) {
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

    public Map<String, Flights> newEngineFlight() {
        List<String> airportsInCityFrom = airportsService.getAllAirportsInCity(searchFormDto.getCityFrom());
        List<String> airportsInCityTo = airportsService.getAllAirportsInCity(searchFormDto.getCityTo());

        String airFrom = airportsInCityFrom.get(0);
        String airTo = airportsInCityTo.get(0);
        //findFlights method return 0;
        if (true) {
            List<String> intersect = flightsRepository.intersectArrivalAirports(airFrom, airTo);

            List<String> MSC = Arrays.asList("SVO", "DME", "VKO");
            if (intersect.size() > 0) {
                //1 Шаг проверка что в промежутке есть москва!
                System.err.println("intersect > 0");
                List<String> moscowFirst = intersect.stream()
                        .filter(MSC::contains)
                        .collect(Collectors.toList());

                if (moscowFirst.size() > 0) {
                    System.err.println("moscowFirst > 0");
                    //если есть, то все доступные рейсы по выбранной дате
                    //Затем сверяем даты
                    Map<String, Flights> msc = fillMap(moscowFirst, airFrom, airTo);
                    System.err.println("STEP 1 MAP SIZE = " + msc.size());
                    if (msc.size() > 0) {
                        return msc;
                    } else {
                        intersect.removeAll(moscowFirst);
                        System.err.println("intersect.removeAll(moscowFirst)");
                        Map<String, Flights> other = fillMap(intersect, airFrom, airTo);
                        System.out.println("STEP 2 MAP SIZE = " + other.size());
                        if (other.size() > 0) return other;
                    }
                }

            }
        }
        return Collections.emptyMap();
    }

    public Map<String, Flights> fillMap(List<String> airports, String airFrom, String airTo) {
        Map<String, Flights> map = new TreeMap<>();
        for (String pivot : airports) {
            List<Flights> first = flightsRepository.findAllFlightsByParam(Collections.singletonList(airFrom), Collections.singletonList(pivot), dayStart().minusDays(1), dayEnd().plusDays(1));
            List<Flights> second = flightsRepository.findAllFlightsByParam(Collections.singletonList(pivot), Collections.singletonList(airTo), dayStart().minusDays(1), dayEnd().plusDays(1));

            if (!first.isEmpty() && !second.isEmpty()) {
                long prevDiff = 24;
                for (Flights f : first) {
                    for (Flights s : second) {

                        Duration duration = Duration.between(f.getArrivalDate(), s.getDepartureDate());
                        long diff = duration.toHours();
                        if (diff >= 1 && diff < 24 && diff < prevDiff) {
                            System.out.println(duration.toHours());
                            Optional<Integer> availableSeats = ticketFlightsRepository.availableSeats(f.getFlightId(),
                                    f.getAircraft(), searchFormDto.getConditions());

                            if (searchFormDto.getNumberOfTickets() <= availableSeats
                                    .orElse(ticketFlightsRepository.allSeats(f.getAircraft(), searchFormDto.getConditions())
                                            .orElse(0))) {
                                String key = "pair " + UUID.randomUUID().toString().substring(0, 6);
                                map.put(key + " first", f);
                                map.put(key + " second", s);
                                prevDiff = diff;
                            }

                        }
                    }
                }
                if (map.size() > 0) return map;
            }
        }
        return Collections.emptyMap();
    }
}


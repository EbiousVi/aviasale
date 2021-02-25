package com.example.aviasale.repository;

import com.example.aviasale.domain.entity.Airports;
import com.example.aviasale.service.FlightService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.locationtech.jts.geom.Point;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class FlightsRepositoryTest {

    @Autowired
    private AirportsRepository airportsRepository;

    @Autowired
    private FlightsRepository flightsRepository;

    @Autowired
    private FlightService flightService;

   /* @Test
    void AAA() {
        List<String> stw = flightsRepository.AAA("STW");
        List<String> pee = flightsRepository.AAA("PEE");
        System.out.println(stw);
        System.out.println(pee);
        stw.retainAll(pee);
        System.out.println(stw);
    }*/

    @Test
    void cal() throws JsonProcessingException {

        List<String> setAirportTo1 = flightsRepository.setAirportTo("STW");
        List<String> setAirportTo2 = flightsRepository.setAirportTo("PEE");

        List<Airports> STW = airportsRepository.findAllByCityContainsIgnoreCase("ставрополь");
        List<Airports> PEE = airportsRepository.findAllByCityContainsIgnoreCase("пермь");

        Point stw = STW.get(0).getPoint();
        Point pee = PEE.get(0).getPoint();
        System.out.println(stw.getX());
        System.out.println(pee.getX());

        double dist = flightService.distFrom(stw.getY(), stw.getX(), pee.getY(), pee.getX());
        if (dist < 400) {
            System.out.println("РАССТОЯНИЕ МАЛО ДЛЯ ПОЛЕТА НА САМОЛЕТЕ!");
        }

    }
}
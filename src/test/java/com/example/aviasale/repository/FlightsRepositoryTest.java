package com.example.aviasale.repository;

import com.example.aviasale.domain.entity.Flights;
import com.example.aviasale.service.NewService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest
class FlightsRepositoryTest {

    @Autowired
    private NewService newService;

    @Autowired
    private FlightsRepository flightsRepository;

   /* @Test
    void cal() {
        Map<String, Flights> map = newService.newEngineFlight();
        System.err.println(map.size() + " ITS OVER");
        map.forEach((k, v) -> {
            System.out.println(v.getFlightId() + " | " + v.getDepartureDate() + " | " + v.getArrivalDate() + " | " + v.getAirportFrom() + " | " + v.getAirportTo());
        });

    }*/

    @Test
    void  t() {
        List<String> list = flightsRepository.intersectArrivalAirports("PEE", "STW");
        System.out.println(list);
    }

}
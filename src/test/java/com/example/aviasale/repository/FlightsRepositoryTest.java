package com.example.aviasale.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FlightsRepositoryTest {
    @Autowired
    private FlightsRepository flightsRepository;

    @Test
    void intersectArrivalAirports() {
        List<String> list = flightsRepository.intersectArrivalAirports(Arrays.asList("KUF"), Arrays.asList("PEE"));
        System.out.println(list);
    }
}
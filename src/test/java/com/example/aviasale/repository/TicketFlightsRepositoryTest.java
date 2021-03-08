package com.example.aviasale.repository;

import com.example.aviasale.domain.entity.Airports;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest
class TicketFlightsRepositoryTest {
    @Autowired
    private AirportsRepository airportsRepository;

    @Test
    @Transactional
    void test() {

    }

}
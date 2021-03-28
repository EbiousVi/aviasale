package com.example.aviasale.repository;

import com.example.aviasale.domain.entity.Airports;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface AirportsRepository extends JpaRepository<Airports, String> {
    Optional<List<Airports>> findAllByCityContainsIgnoreCase(String city);

    Optional<List<Airports>> findAllBy();

    @Query("SELECT a from Flights f join Airports a on f.airportFrom = a.airportCode where f.flightId =:flightId")
    Optional<Airports> findAirportFromByFlightId(Integer flightId);

    @Query("SELECT a from Flights f join Airports a on f.airportTo = a.airportCode where f.flightId =:flightId")
    Optional<Airports> findAirportToByFlightId(Integer flightId);

    Optional<Airports> findByAirportCode(String airportCode);
}

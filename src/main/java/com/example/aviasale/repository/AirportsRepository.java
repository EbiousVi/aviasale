package com.example.aviasale.repository;

import com.example.aviasale.domain.entity.Airports;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AirportsRepository extends JpaRepository<Airports, String> {
    List<Airports> findAllByCityContainsIgnoreCase(String city);

    List<Airports> findAllBy();

    @Query("SELECT a from Flights f join Airports a on f.airportFrom = a.airportCode where f.flightId =:flightId")
    Airports findAirportFromByFlightId(Integer flightId);

    @Query("SELECT a from Flights f join Airports a on f.airportTo = a.airportCode where f.flightId =:flightId")
    Airports findAirportToByFlightId(Integer flightId);
}

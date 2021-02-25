package com.example.aviasale.repository;

import com.example.aviasale.domain.entity.Flights;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FlightsRepository extends JpaRepository<Flights, Integer> {

    @Query("select f from Flights f where f.airportFrom in :airportsInCityFrom " +
            "and f.airportTo in :airportsInCityTo " +
            "and f.departureDate between :date1 and :date2 " +
            "and f.status = 'Scheduled' " +
            "order by f.departureDate")
    List<Flights> findAllFlightsByParam(@Param("airportsInCityFrom") List<String> airportsInCityFrom,
                                        @Param("airportsInCityTo") List<String> airportsInCityTo,
                                        @Param("date1") LocalDateTime date1,
                                        @Param("date2") LocalDateTime date2);

    Flights findByFlightId(Integer flightId);


    @Query("select distinct f.airportTo from Flights f where f.airportFrom =:param")
    List<String> setAirportTo(String param);

}

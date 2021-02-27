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


    @Query(value = "select distinct arrival_airport from flights\n" +
            "where departure_airport =:depAir1 \n" +
            "INTERSECT\n" +
            "select distinct arrival_airport from flights\n" +
            "where departure_airport =:depAir2", nativeQuery = true)
    List<String> intersectArrivalAirports(@Param("depAir1") String depAir1, @Param("depAir2") String depAir2);

    @Query("select f from Flights f where f.airportFrom =:airportFrom and f.airportTo =:airportTo and f.departureDate =:date")
    Flights findByAirportsAndDate(@Param("airportFrom") String airportFrom,
                                  @Param("airportTo") String airportTo,
                                  @Param("date") LocalDateTime date);

}

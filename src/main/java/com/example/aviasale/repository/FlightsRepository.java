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
import java.util.Optional;

@Repository
public interface FlightsRepository extends JpaRepository<Flights, Integer> {

    Optional<Flights> findByFlightId(Integer flightId);

    @Query("select f from Flights f where f.airportFrom in :airportsInCityFrom " +
            "and f.airportTo in :airportsInCityTo " +
            "and f.departureDate between :date1 and :date2 " +
            "and f.status = 'Scheduled' " +
            "order by f.departureDate")
    Optional<List<Flights>> findFlightsWithSearchParams(@Param("airportsInCityFrom") List<String> airportsInCityFrom,
                                                        @Param("airportsInCityTo") List<String> airportsInCityTo,
                                                        @Param("date1") LocalDateTime date1,
                                                        @Param("date2") LocalDateTime date2);

    @Query(value = "select distinct arrival_airport from flights\n" +
            "where departure_airport  in :start \n" +
            "INTERSECT\n" +
            "select distinct arrival_airport from flights\n" +
            "where departure_airport in :end", nativeQuery = true)
    Optional<List<String>> intersectArrivalAirports(@Param("start") List<String> start, @Param("end") List<String> end);

    @Query(value = "select round(avg(a) - avg(a)%100) from \n" +
            "(select amount as a from flights\n" +
            "join ticket_flights using(flight_id)\n" +
            "group by flight_no, flight_id, amount, fare_conditions\n" +
            "having flight_no =:flightNo \n" +
            "order by flight_no) as avg_by_flight_no", nativeQuery = true)
    Optional<Double> avgPriceByFlightNo(@Param("flightNo") String FlightNo);

    @Query(value = "select * from flights\n" +
            "where flights.flight_id = (select flight_id from ticket_flights\n" +
            "where ticket_no =:ticketNo)", nativeQuery = true)
    Optional<Flights> findFlightByTicketNo(@Param("ticketNo") String ticketNo);
}

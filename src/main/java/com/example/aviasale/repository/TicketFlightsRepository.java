package com.example.aviasale.repository;

import com.example.aviasale.domain.entity.TicketFlights;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface TicketFlightsRepository extends JpaRepository<TicketFlights, TicketFlights.CompositeKey> {

    /**
     * Check available seats. Each aircraft model has only one cabin layout. First find all seats by aircraft
     * and fare conditions. After find all reserved seats by flight id and fare conditions.
     * And calculate the difference.
     */

    @Query(value = "select cast(" +
            "(select count(*) from seats \n" +
            "where aircraft_code =:aircraftCode \n" +
            "and fare_conditions =:fareConditions \n" +
            "group by fare_conditions) - count(*) as integer) from ticket_flights\n" +
            "where flight_id =:flightId and fare_conditions =:fareConditions\n" +
            "group by fare_conditions", nativeQuery = true)
    Optional<Integer> freeSeats(@Param("flightId") Integer flightId,
                                @Param("aircraftCode") String aircraftCode,
                                @Param("fareConditions") String fareConditions);

    @Query("select count(s) from AircraftLayout s " +
            "where s.compositeKey.aircraftCode =:aircraftCode " +
            "and s.fareConditions =:fareConditions " +
            "group by s.fareConditions")
    Optional<Integer> allSeats(@Param("aircraftCode") String aircraftCode,
                               @Param("fareConditions") String fareConditions);

    @Query("select tf from TicketFlights tf where tf.compositeKey.ticketNumber =:ticketNo")
    Optional<TicketFlights> findByTicketNo(@Param("ticketNo") String ticketNo);

    @Query(value = "select round(avg(a) - avg(a)%100)::int4 from \n" +
            "(select amount as a from flights\n" +
            "join ticket_flights using(flight_id)\n" +
            "group by flight_no, flight_id, amount, fare_conditions\n" +
            "having flight_no =:flightNo \n" +
            "order by flight_no) as avg_by_flight_no", nativeQuery = true)
    Optional<Integer> avgPriceByFlightNo(@Param("flightNo") String FlightNo);

}

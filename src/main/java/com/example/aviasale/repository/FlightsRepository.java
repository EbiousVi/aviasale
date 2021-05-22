package com.example.aviasale.repository;

import com.example.aviasale.domain.entity.Flights;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FlightsRepository extends JpaRepository<Flights, Integer> {

    @Query("select f from Flights f order by f.flightId")
    List<Flights> findAllFlights();

    Optional<Flights> findByFlightId(Integer flightId);

    @Query("select f from Flights f " +
            "where f.airportFrom in :airportsInCityFrom " +
            "and f.airportTo in :airportsInCityTo " +
            "and f.departureDate between :dayStart and :dayEnd " +
            "and f.status = 'Scheduled' " +
            "order by f.departureDate")
    List<Flights> findFlightsBySearchParams(@Param("airportsInCityFrom") List<String> airportsInCityFrom,
                                            @Param("airportsInCityTo") List<String> airportsInCityTo,
                                            @Param("dayStart") LocalDateTime dayStart,
                                            @Param("dayEnd") LocalDateTime dayEnd);

    @Query(value = "select * from flights\n" +
            "where flights.flight_id = (select flight_id from ticket_flights\n" +
            "where ticket_no =:ticketNo)", nativeQuery = true)
    Optional<Flights> findByTicketNo(@Param("ticketNo") String ticketNo);


    /**
     * Intersect arrival airports need to find Connecting flight
     */
    @Query(value = "select distinct arrival_airport from flights\n" +
            "where departure_airport  in :first \n" +
            "INTERSECT\n" +
            "select distinct arrival_airport from flights\n" +
            "where departure_airport in :second", nativeQuery = true)
    List<String> findIntersectArrivalAirports(@Param("first") List<String> first,
                                              @Param("second") List<String> second);

    /**
     * DB don't have information about flight cost. This query count avg cost
     * for already purchased tickets. And round to hundredths;
     */
    @Query(value = "select round(avg(a) - avg(a)%100) from \n" +
            "(select amount as a from flights\n" +
            "join ticket_flights using(flight_id)\n" +
            "group by flight_no, flight_id, amount, fare_conditions\n" +
            "having flight_no =:flightNo \n" +
            "order by flight_no) as avg_by_flight_no", nativeQuery = true)
    Optional<Double> avgPriceByFlightNo(@Param("flightNo") String FlightNo);
}

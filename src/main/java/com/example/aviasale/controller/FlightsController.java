package com.example.aviasale.controller;

import com.example.aviasale.domain.pojo.Price;
import com.example.aviasale.domain.dto.apiDto.BookingsDto;
import com.example.aviasale.domain.dto.apiDto.PassengersData;
import com.example.aviasale.domain.dto.apiDto.SearchFormDto;
import com.example.aviasale.domain.entity.Airports;
import com.example.aviasale.domain.entity.User;
import com.example.aviasale.expection.BookingFailedException;
import com.example.aviasale.service.AirportsService;
import com.example.aviasale.service.BookingService;
import com.example.aviasale.service.FlightService;
import com.example.aviasale.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class FlightsController {
    private final FlightService flightService;
    private final AirportsService airportsService;
    private final BookingService bookingService;
    private final UserService userService;

    private Price price;
    private SearchFormDto searchFormDto;

 /*   private static final Logger logger
            = LoggerFactory.getLogger(FlightsController.class);*/

    @Autowired
    public FlightsController(FlightService flightService, AirportsService airportsService, BookingService bookingService, UserService userService) {
        this.flightService = flightService;
        this.airportsService = airportsService;
        this.bookingService = bookingService;
        this.userService = userService;
    }

    @GetMapping("/airports")
    public List<String> airports() {
        List<Airports> list = airportsService.allAirports();
        return list.stream().map(Airports::getCity).sorted().distinct().collect(Collectors.toList());
    }

    @GetMapping("/bookings")
    public List<BookingsDto> getBookingsByEmail(Principal principal) {
        User user = userService.findByEmail(principal.getName());
        return bookingService.getAllBookingDtoByUser(user);
    }

    @PostMapping("/flights")
    public ResponseEntity<?> getFlights(@RequestBody SearchFormDto searchFormDto) {
        this.searchFormDto = searchFormDto;
        flightService.setSearchFormDto(searchFormDto);
        return flightService.getFlights();
    }

    @GetMapping("/prepare-booking")
    @ResponseStatus(code = HttpStatus.OK)
    public void prepareBooking(@RequestParam("flightId") Integer flightId,
                               @RequestParam("value") Double value) {
        Price price = new Price();
        price.setFlightId(flightId);
        price.setValue(BigDecimal.valueOf(value));
        this.price = price;
    }

    @PostMapping("/booking")
    public ResponseEntity<?> booking(@RequestBody List<PassengersData> passengersData, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        try {
            bookingService.prepareBooking(user, passengersData, searchFormDto, price);
            List<BookingsDto> allBookingDtoByUser = bookingService.getAllBookingDtoByUser(user);
            return ResponseEntity.ok(allBookingDtoByUser);
        } catch (BookingFailedException e) {
            return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete-booking/{bookRef}")
    @ResponseStatus(code = HttpStatus.OK)
    public void deleteBooking(@PathVariable("bookRef") String bookRef) {
        bookingService.deleteBooking(bookingService.deleteBookingRelations(bookRef));
    }

    @DeleteMapping("/remove-passenger/{ticketNumber}")
    @ResponseStatus(code = HttpStatus.OK)
    public void removePassenger(@PathVariable("ticketNumber") String ticketNumber) {
        System.out.println(ticketNumber);
        bookingService.removePassenger(ticketNumber);
    }
}

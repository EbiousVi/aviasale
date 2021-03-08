package com.example.aviasale.service;

import com.example.aviasale.domain.entity.Bookings;
import com.example.aviasale.domain.entity.User;
import com.example.aviasale.domain.pojo.Price;
import com.example.aviasale.expection.BookingFailedException;
import com.example.aviasale.repository.BookingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class BookingsService {

    private final BookingsRepository bookingsRepository;

    @Autowired
    public BookingsService(BookingsRepository bookingsRepository) {
        this.bookingsRepository = bookingsRepository;
    }

    public Bookings createBooking(User user, List<Price> prices, Integer numberOfTickets) throws BookingFailedException {
        Bookings booking = new Bookings();
        booking.setBookDate(LocalDateTime.now());
        booking.setBookRef(UUID.randomUUID().toString().substring(0, 6));
        Double sum = prices.stream()
                .map(x -> x.getValue().doubleValue())
                .reduce(Double::sum)
                .orElseThrow(() -> new BookingFailedException("Price is Empty", HttpStatus.NOT_FOUND));
        booking.setTotalAmount(sum * numberOfTickets);
        booking.setUser(user);
        bookingsRepository.save(booking);

        System.err.println(1);
        System.err.println(booking);
        return booking;
    }

    @Transactional
    public void deleteBooking(Bookings booking) {
        bookingsRepository.delete(booking);
    }

    public Bookings getBookingByBookRef(String bookRef) {
        return bookingsRepository.findByBookRef(bookRef);
    }

    public List<Bookings> getBookingsByUser(User user) {
        return bookingsRepository.findAllByUser(user);
    }

}

package com.example.aviasale.domain.dto.apiDto;

import com.example.aviasale.domain.entity.Bookings;
import com.example.aviasale.domain.entity.Tickets;

import java.util.List;

public class BookingsDto {
    private Bookings booking;
    private List<Tickets> tickets;

    public BookingsDto() {
    }

    @Override
    public String toString() {
        return "userBookings{" +
                ", booking=" + booking +
                ", tickets=" + tickets +
                '}';
    }

    public Bookings getBooking() {
        return booking;
    }

    public void setBooking(Bookings booking) {
        this.booking = booking;
    }

    public List<Tickets> getTickets() {
        return tickets;
    }

    public void setTickets(List<Tickets> tickets) {
        this.tickets = tickets;
    }
}

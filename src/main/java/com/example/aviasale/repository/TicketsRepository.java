package com.example.aviasale.repository;

import com.example.aviasale.domain.entity.Tickets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface TicketsRepository extends JpaRepository<Tickets, String> {
    @Modifying
    void deleteByTicketNumber(String ticketNumber);

    List<Tickets> findByBookRef(String bookRef);
}

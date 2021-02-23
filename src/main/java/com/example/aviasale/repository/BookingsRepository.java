package com.example.aviasale.repository;

import com.example.aviasale.domain.entity.Bookings;
import com.example.aviasale.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingsRepository extends JpaRepository<Bookings, String> {
    @Modifying
    @Query("delete from Bookings b where b.bookRef =:bookRef")
    void deleteByBookRef(String bookRef);

    Bookings findByBookRef(String bookRef);

    @Query("select b from Bookings b join User u on b.user.id = u.id where u =:user")
    List<Bookings> findAllByUser(@Param("user") User user);
}

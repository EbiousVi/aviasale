package com.example.aviasale.repository;

import com.example.aviasale.domain.entity.AircraftLayout;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AircraftLayoutRepository extends JpaRepository<AircraftLayout, Integer> {
    List<AircraftLayout> findAllByCompositeKey_AircraftCode(String aircraftCode);
}

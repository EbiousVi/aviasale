package com.example.aviasale.controller;

import com.example.aviasale.domain.dto.apiDto.SearchFormDto;
import com.example.aviasale.domain.entity.Flights;
import com.example.aviasale.service.NewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class Super {
    private final NewService newService;

    @Autowired
    public Super(NewService newService) {
        this.newService = newService;
    }

    @PostMapping("/super")
    public Map<String, Flights> getFlights(@RequestBody SearchFormDto searchFormDto) {
        newService.setSearchFormDto(searchFormDto);
        return newService.newEngineFlight();
    }
}

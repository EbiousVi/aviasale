package com.example.aviasale.service.dtoPacker;

import com.example.aviasale.domain.dto.apiDto.OneWayDirectFlightDto;
import com.example.aviasale.domain.entity.Flights;
import com.example.aviasale.service.AirportsService;
import com.example.aviasale.service.PriceService;
import com.example.aviasale.service.SearchQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OneWayDirectFlightPackager implements FlightsDtoPackager<OneWayDirectFlightDto, Flights> {
    private final AirportsService airportsService;
    private final PriceService priceService;
    private final SearchQueryService searchQueryService;

    @Autowired
    public OneWayDirectFlightPackager(AirportsService airportsService, PriceService priceService, SearchQueryService searchQueryService) {
        this.airportsService = airportsService;
        this.priceService = priceService;
        this.searchQueryService = searchQueryService;
    }

    @Override
    public List<OneWayDirectFlightDto> wrapToDto(List<Flights> list) {
        if (list.isEmpty()) return Collections.emptyList();
        return list.stream().map(flight -> {
            OneWayDirectFlightDto dto = new OneWayDirectFlightDto();
            dto.setFlight(flight);
            dto.setAirportFrom(airportsService.getAirportFrom(flight.getFlightId()));
            dto.setAirportTo(airportsService.getAirportTo(flight.getFlightId()));
            dto.setPrice(priceService.createPrice(flight.getFlightId(), flight.getFlightNo(),
                    searchQueryService.getSearchQueryDto().getConditions()));
            dto.setDirect(true);
            return dto;
        }).collect(Collectors.toList());
    }
}

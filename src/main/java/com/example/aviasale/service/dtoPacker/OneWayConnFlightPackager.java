package com.example.aviasale.service.dtoPacker;

import com.example.aviasale.domain.dto.apiDto.OneWayConnFlightDto;
import com.example.aviasale.domain.entity.Airports;
import com.example.aviasale.domain.pojo.ConnFlight;
import com.example.aviasale.service.AirportsService;
import com.example.aviasale.service.PriceService;
import com.example.aviasale.service.SearchQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OneWayConnFlightPackager implements FlightsDtoPackager<OneWayConnFlightDto, ConnFlight> {
    private final PriceService priceService;
    private final SearchQueryService searchQuery;
    private final AirportsService airportsService;

    @Autowired
    public OneWayConnFlightPackager(PriceService priceService, SearchQueryService searchQuery, AirportsService airportsService) {
        this.priceService = priceService;
        this.searchQuery = searchQuery;
        this.airportsService = airportsService;
    }

    @Override
    public List<OneWayConnFlightDto> wrapToDto(List<ConnFlight> list) {
        if (list.isEmpty()) return Collections.emptyList();
        return list.stream().map(f -> {
            OneWayConnFlightDto dto = new OneWayConnFlightDto();
            dto.setFlight1(f.getFirst());
            dto.setFlight2(f.getSecond());
            dto.setPrice1(priceService.createPrice(
                    f.getFirst().getFlightId(),
                    f.getFirst().getFlightNo(),
                    searchQuery.getSearchQueryDto().getConditions()));
            dto.setPrice2(priceService.createPrice(
                    f.getSecond().getFlightId(),
                    f.getSecond().getFlightNo(),
                    searchQuery.getSearchQueryDto().getConditions()));
            Airports airFrom1 = airportsService.getAirportFrom(f.getFirst().getFlightId());
            Airports airTo1 = airportsService.getAirportTo(f.getFirst().getFlightId());
            dto.setAirFrom1(airFrom1);
            dto.setAirTo1(airTo1);
            Airports airFrom2 = airportsService.getAirportFrom(f.getSecond().getFlightId());
            Airports airTo2 = airportsService.getAirportTo(f.getSecond().getFlightId());
            dto.setAirFrom2(airFrom2);
            dto.setAirTo2(airTo2);
            dto.setConn(true);
            return dto;
        }).collect(Collectors.toList());
    }
}

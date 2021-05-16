package com.example.aviasale.controller;

import com.example.aviasale.domain.dto.apiDto.SearchQueryDto;
import com.example.aviasale.service.searchEngine.searchEngine.OneWaySearchEngineService;
import com.example.aviasale.service.SearchQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/one-way")
public class OneWayFlightController {

    private final SearchQueryService searchQuery;
    private final OneWaySearchEngineService oneWaySearchEngine;

    @Autowired
    public OneWayFlightController(SearchQueryService searchQuery,
                                  OneWaySearchEngineService oneWaySearchEngine) {
        this.searchQuery = searchQuery;
        this.oneWaySearchEngine = oneWaySearchEngine;
    }

    @PostMapping("/flight")
    public ResponseEntity<?> findFlight(@RequestBody SearchQueryDto searchQueryDto) {
        searchQuery.setSearchQueryDto(searchQueryDto);
        return oneWaySearchEngine.getResult();
    }
}

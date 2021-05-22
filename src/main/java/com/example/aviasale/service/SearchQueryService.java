package com.example.aviasale.service;

import com.example.aviasale.domain.dto.apiDto.SearchQueryDto;
import com.example.aviasale.domain.pojo.Price;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SearchQueryService {
    private SearchQueryDto searchQueryDto;
    private List<Price> prices;

    public SearchQueryDto getSearchQueryDto() {
        return searchQueryDto;
    }

    public void setSearchQueryDto(SearchQueryDto searchQueryDto) {
        this.searchQueryDto = searchQueryDto;
    }

    public List<Price> getPrices() {
        return prices;
    }

    public void setPrices(List<Price> prices) {
        this.prices = prices;
    }

    public LocalDateTime searchDateStart() {
        return LocalDateTime.parse(searchQueryDto.getDate() + "T00:00:00");
    }

    public LocalDateTime searchDateEnd() {
        return LocalDateTime.parse(searchQueryDto.getDate() + "T23:59:59");
    }
}

package com.example.aviasale.service;

import com.example.aviasale.domain.dto.apiDto.SearchQueryDto;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SearchQueryService {
    private SearchQueryDto searchQueryDto;

    public SearchQueryDto getSearchQueryDto() {
        return searchQueryDto;
    }

    public void setSearchQueryDto(SearchQueryDto searchQueryDto) {
        this.searchQueryDto = searchQueryDto;
    }

    public LocalDateTime searchDateStart() {
        return LocalDateTime.parse(searchQueryDto.getDate() + "T00:00:00");
    }

    public LocalDateTime searchDateEnd() {
        return LocalDateTime.parse(searchQueryDto.getDate() + "T23:59:59");
    }
}

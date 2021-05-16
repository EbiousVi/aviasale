package com.example.aviasale.service.searchEngine.searchEngine;

import org.springframework.http.ResponseEntity;

public interface SearchEngine {
    ResponseEntity<?> getResult();
}

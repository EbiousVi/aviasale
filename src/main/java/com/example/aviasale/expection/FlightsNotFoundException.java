package com.example.aviasale.expection;

import org.springframework.http.HttpStatus;

public class FlightsNotFoundException extends CustomException {
    public FlightsNotFoundException(String msg, HttpStatus httpStatus) {
        super(msg, httpStatus);
    }
}

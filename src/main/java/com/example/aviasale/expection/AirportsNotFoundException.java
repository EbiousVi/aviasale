package com.example.aviasale.expection;

import org.springframework.http.HttpStatus;

public class AirportsNotFoundException extends CustomException {
    public AirportsNotFoundException(String msg, HttpStatus httpStatus) {
        super(msg, httpStatus);
    }
}

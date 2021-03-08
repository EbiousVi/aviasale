package com.example.aviasale.expection;

import org.springframework.http.HttpStatus;

public class TicketFlightsNotFoundException extends CustomException {
    public TicketFlightsNotFoundException(String msg, HttpStatus httpStatus) {
        super(msg, httpStatus);
    }
}

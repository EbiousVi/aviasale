package com.example.aviasale.expection;

import org.springframework.http.HttpStatus;

public class BookingFailedException extends Exception {
    private String message;
    private HttpStatus httpStatus;

    public BookingFailedException(String msg, HttpStatus httpStatus) {
        super(msg);
        this.httpStatus = httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}

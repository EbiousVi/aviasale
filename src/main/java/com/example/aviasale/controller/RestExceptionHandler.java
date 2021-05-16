package com.example.aviasale.controller;

import com.example.aviasale.expection.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    //Барбара Лисков на месте!
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleCustomException(CustomException ex) {
        ex.printStackTrace();
        return ResponseEntity.status(ex.getHttpStatus()).body(ex.getMessage());
    }
}

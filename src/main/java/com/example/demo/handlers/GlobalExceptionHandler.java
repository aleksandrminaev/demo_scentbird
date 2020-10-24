package com.example.demo.handlers;

import com.example.demo.covid.dtos.APIError;
import com.example.demo.covid.dtos.CovidException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpClientErrorException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public APIError notFound(HttpClientErrorException ex) {
        String message = "Could not find any data on your request!";
        return new APIError(HttpStatus.NOT_FOUND.value(), message);
    }

    @ExceptionHandler(CovidException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public APIError covidException(CovidException ex) {
        String message = ex.getMessage();
        return new APIError(HttpStatus.INTERNAL_SERVER_ERROR.value(), message);
    }

    // More exception handlers here ...
}
package com.example.demo.covid.dtos;

import java.time.Instant;

public class CovidException extends RuntimeException {
    public CovidException(String message) {
        super(message);
    }
}

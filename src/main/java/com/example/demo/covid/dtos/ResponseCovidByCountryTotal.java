package com.example.demo.covid.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
public class ResponseCovidByCountryTotal {
    private String country;
    private LocalDate date;
    private Long confirmed;
}

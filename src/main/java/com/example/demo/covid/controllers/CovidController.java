package com.example.demo.covid.controllers;

import com.example.demo.covid.dtos.ResponseCovidByCountryTotal;
import com.example.demo.covid.services.CovidService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;


@RestController
@RequestMapping("/covid")
@RequiredArgsConstructor
public class CovidController {

    private final CovidService covidService;

    @GetMapping("/total/countries")
    public List<ResponseCovidByCountryTotal> getByCountryTotal(@RequestParam("countries") List<String> countries,
                                                               @RequestParam("from")
                                                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                                               @RequestParam("to")
                                                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        return covidService.getByCountryTotal(countries, from.atStartOfDay(), LocalDateTime.of(to, LocalTime.MAX));
    }

}

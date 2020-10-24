package com.example.demo.covid.services;

import com.example.demo.covid.dtos.CovidByCountryTotal;
import com.example.demo.covid.dtos.CovidException;
import com.example.demo.covid.dtos.ResponseCovidByCountryTotal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CovidService {

    private final String host = "https://api.covid19api.com";
    private final RestTemplate restTemplate;
    private final CacheService cacheService;

    public List<ResponseCovidByCountryTotal> getByCountryTotal(List<String> countries, LocalDateTime from, LocalDateTime to) {

        log.info("invoke getByCountryTotal for {} with interval {}, {}", countries, from.toString(), to.toString());

        if (countries.isEmpty()) {
            throw new CovidException("Provide list of countries !");
        }
        if (to.isBefore(from)) {
            throw new CovidException("Incorrect interval ! Please check your dates in request.");
        }

        Set<String> queries = countries.stream()
                .map(c -> generateUrlToGetByCountryTotal(c, from, to)).collect(Collectors.toSet());

        String key = cacheService.calculateKey(queries);

        List<ResponseCovidByCountryTotal> cacheResult = (List<ResponseCovidByCountryTotal>) cacheService.getFromCache(key);
        if (Objects.nonNull(cacheResult)) {
            return cacheResult;
        }

        List<ResponseCovidByCountryTotal> result = getByCountriesTotal(queries);

        cacheService.storeInCache(key, result);
        log.info("invoke getByCountryTotal for {} with interval {}, {} was successful.", countries, from.toString(), to.toString());

        return result;
    }

    private String generateUrlToGetByCountryTotal(String country, LocalDateTime from, LocalDateTime to) {
        return UriComponentsBuilder.fromUriString(host + "/total/country/{country}/status/confirmed")
                .queryParam("from", DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(from))
                .queryParam("to", DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(to))
                .buildAndExpand(Map.of("country", country)).toUriString();
    }

    private List<ResponseCovidByCountryTotal> getByCountriesTotal(Collection<String> queries) {
        return queries.stream()
                .map(url -> {
                    ResponseEntity<List<CovidByCountryTotal>> responseEntity =
                            restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                            });

                    return Objects.requireNonNull(responseEntity.getBody()).stream()
                            .map(r -> new ResponseCovidByCountryTotal(
                                    r.getCountry(),
                                    LocalDate.ofInstant(Instant.parse(r.getDate()), ZoneOffset.UTC),
                                    r.getCases()))
                            .collect(Collectors.toList());
                })
                .flatMap(Collection::stream)
                .sorted(Comparator.comparing(ResponseCovidByCountryTotal::getCountry))
                .collect(Collectors.toList());
    }
}

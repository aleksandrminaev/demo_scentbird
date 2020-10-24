package com.example.demo.covid.services;

import com.example.demo.DemoApplication;
import com.example.demo.covid.dtos.CovidByCountryTotal;
import com.example.demo.covid.dtos.CovidException;
import com.example.demo.covid.dtos.ResponseCovidByCountryTotal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = DemoApplication.class)
public class CovidServiceTest {
    @Mock
    CacheService cacheService;
    @Mock
    RestTemplate restTemplate;
    CovidService covidService;

    @BeforeEach
    public void init() {
        covidService = new CovidService(restTemplate, cacheService);
    }

    @Test
    void getByCountryTotalExceptionNoListWithCountriesTest() {
        Exception exception = assertThrows(CovidException.class,
                () -> covidService.getByCountryTotal(List.of(), LocalDateTime.now(), LocalDateTime.now()));

        String expectedMessage = "Provide list of countries !";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void getByCountryTotalExceptionIncorrectDatesTest() {
        Exception exception = assertThrows(CovidException.class,
                () -> covidService.getByCountryTotal(List.of("Canada"), LocalDateTime.now(), LocalDateTime.now().minusDays(1L)));

        String expectedMessage = "Incorrect interval ! Please check your dates in request.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void getByCountryTotalFromCacheTest() {
        var result = List.of(new ResponseCovidByCountryTotal("Canada", LocalDate.now(), 1L));
        when(cacheService.getFromCache(any())).thenReturn(result);
        var invokeResult = covidService.getByCountryTotal(List.of("Canada"), LocalDateTime.now(), LocalDateTime.now());
        assertThat(result).isEqualTo(invokeResult);
    }

    @Test
    void getByCountryTotalTest() {
        var result = List.of(new ResponseCovidByCountryTotal("Canada", LocalDate.parse("2020-10-10"), 100L));
        var covid = new CovidByCountryTotal("Canada", 100L, null, "2020-10-10T00:00:00Z");
        ResponseEntity<List<CovidByCountryTotal>> myEntity = new ResponseEntity<>(List.of(covid), HttpStatus.ACCEPTED);
        when(cacheService.getFromCache(any())).thenReturn(null);
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(),  ArgumentMatchers.<ParameterizedTypeReference<List<CovidByCountryTotal>>>any())).thenReturn(myEntity);
        var invokeResult = covidService.getByCountryTotal(List.of("Canada"), LocalDateTime.now(), LocalDateTime.now());
        assertEquals(result.get(0).getCountry(), invokeResult.get(0).getCountry());
        assertEquals(result.get(0).getConfirmed(), invokeResult.get(0).getConfirmed());
        assertEquals(result.get(0).getDate(), invokeResult.get(0).getDate());
    }
}

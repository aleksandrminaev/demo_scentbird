package com.example.demo.covid.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CovidByCountryTotal {
    @JsonProperty("Country")
    private String country;
    @JsonProperty("Cases")
    private Long cases;
    @JsonProperty("Status")
    private String status;
    @JsonProperty("Date")
    private String date;
}

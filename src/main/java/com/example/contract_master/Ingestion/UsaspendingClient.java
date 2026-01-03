package com.example.contract_master.Ingestion;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;

@Component
public class UsaspendingClient {

  private static final String URL =
      "https://api.usaspending.gov/api/v2/search/spending_by_award/";

  private final RestTemplate restTemplate;

  public UsaspendingClient(RestTemplateBuilder builder) {
    this.restTemplate = builder
        .setConnectTimeout(Duration.ofSeconds(10))
        .setReadTimeout(Duration.ofSeconds(60))
        .build();
  }

  public JsonNode fetchAwards(LocalDate start, LocalDate end, int page) {
    Map<String, Object> body = Map.of(
        "filters", Map.of(
            "time_period", List.of(Map.of(
                "start_date", start.toString(),
                "end_date", end.toString()
            )),
            "award_type_codes", List.of("A", "B", "C", "D")
        ),
        "limit", 100,
        "page", page,
        "fields", List.of(
            "Award ID",
            "Recipient Name",
            "Start Date",
            "End Date",
            "Award Amount",
            "Awarding Agency",
            "Awarding Sub Agency",
            "Contract Award Type",
            "Award Type",
            "Funding Agency",
            "Funding Sub Agency",
            "Primary Place of Performance"
        )
    );

    return restTemplate.postForObject(URL, body, JsonNode.class);
  }
}

package com.example.contract_master.api;

import java.time.LocalDate;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.contract_master.Ingestion.AwardIngestionJob;

@RestController
@RequestMapping("/api/ingestion")
public class IngestionController {

  private final AwardIngestionJob job;

  public IngestionController(AwardIngestionJob job) {
    this.job = job;
  }

  @PostMapping("/awards")
  public String ingestAwards(
      @RequestParam String start,
      @RequestParam String end
  ) throws Exception {
    job.ingest(LocalDate.parse(start), LocalDate.parse(end));
    return "OK";
  }
}

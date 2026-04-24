package com.example.contract_master.api;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
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

  // Example:
  // /api/ingestion/awards?start=2024-01-01&end=2024-12-31
  @PostMapping("/awards")
  public String ingestAwards(
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
  ) throws Exception {
    job.ingest(start, end);
    return "Ingestion complete: " + start + " -> " + end;
  }
}

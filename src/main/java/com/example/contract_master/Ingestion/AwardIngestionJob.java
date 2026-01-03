package com.example.contract_master.Ingestion;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.contract_master.analytics.DuckParquetWriter;
import com.fasterxml.jackson.databind.JsonNode;

@Component
public class AwardIngestionJob {

  private final UsaspendingClient client;
  private final AwardNormalizer normalizer;
  private final DuckParquetWriter parquetWriter; // next step

  public AwardIngestionJob(UsaspendingClient client, AwardNormalizer normalizer, DuckParquetWriter parquetWriter) {
    this.client = client;
    this.normalizer = normalizer;
    this.parquetWriter = parquetWriter;
  }

  // weekly at 2am Sunday
  @Scheduled(cron = "0 0 2 * * SUN")
  public void ingestWeekly() throws Exception {
    ingest(LocalDate.now().minusYears(1).withDayOfYear(1), LocalDate.now());
  }

  // Call this from a controller for manual runs (optional)
  public void ingest(LocalDate start, LocalDate end) throws Exception {
    int page = 1;
    boolean hasMore = true;

    List<AwardRow> all = new ArrayList<>();

    while (hasMore) {
      JsonNode resp = client.fetchAwards(start, end, page);
      JsonNode results = resp.get("results");
      all.addAll(normalizer.normalize(results));

      hasMore = resp.path("page_metadata").path("has_next").asBoolean(false);
      page++;
    }

    // write csv → convert to parquet
    Path csv = Path.of("storage/parquet/awards/awards_normalized.csv");
    normalizer.writeNormalizedCsv(all, csv);
    parquetWriter.csvToParquet(csv, Path.of("storage/parquet/awards/awards_latest.parquet"));
  }
}


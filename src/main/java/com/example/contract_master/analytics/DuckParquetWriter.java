package com.example.contract_master.analytics;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.stereotype.Component;

@Component
public class DuckParquetWriter {

  private final DataSource dataSource;

  public DuckParquetWriter(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public void csvToParquet(Path csvPath, Path parquetPath) throws Exception {
    try (Connection conn = dataSource.getConnection();
         Statement stmt = conn.createStatement()) {

      // Read CSV -> temp table, then COPY to parquet
      stmt.execute("""
        CREATE OR REPLACE TABLE temp_awards AS
        SELECT
          "Award ID"                AS award_id,
          "Recipient Name"          AS recipient_name,
          "Recipient UEI"           AS recipient_uei,
          "Awarding Agency"         AS awarding_agency,
          "Contract Vehicle"        AS vehicle_normalized,
          "Award Amount"::DOUBLE    AS award_amount,
          "Total Potential Value"::DOUBLE AS potential_total_amount,
          "Start Date"::DATE        AS start_date,
          "End Date"::DATE          AS end_date,
          "Is Active"::BOOLEAN      AS is_active,
          "Months to Expiration"::INTEGER AS months_to_expiration,
          "Is Prime"::BOOLEAN       AS is_prime,
          "Likely Recompete"::BOOLEAN AS likely_recompete
        FROM read_csv_auto('%s', header=true);
      """.formatted(csvPath.toString()));

      stmt.execute("""
        COPY (SELECT * FROM temp_awards)
        TO '%s' (FORMAT PARQUET);
      """.formatted(parquetPath.toString()));
    }
  }
}


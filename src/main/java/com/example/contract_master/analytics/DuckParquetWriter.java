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
          column0  AS award_id,
          column1  AS recipient_name,
          column2::DATE AS start_date,
          column3::DATE AS end_date,
          column4::DOUBLE AS award_amount,
          column5  AS awarding_agency,
          column6  AS awarding_sub_agency,
          column7::BOOLEAN AS is_active,
          column8::INTEGER AS months_to_expiration
        FROM read_csv_auto('%s', header=false);
      """.formatted(csvPath.toString()));

      stmt.execute("""
        COPY (SELECT * FROM temp_awards)
        TO '%s' (FORMAT PARQUET);
      """.formatted(parquetPath.toString()));
    }
  }
}


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
        SELECT * FROM read_csv_auto('%s', header=true);
      """.formatted(csvPath.toString()));

      stmt.execute("""
        COPY (SELECT * FROM temp_awards)
        TO '%s' (FORMAT PARQUET);
      """.formatted(parquetPath.toString()));
    }
  }
}


package com.example.contract_master.analytics;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.Statement;
import java.util.stream.Stream;

import javax.sql.DataSource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DuckDbInitializer {

  private static final Path PARQUET_DIR = Path.of("storage/parquet/awards");

  @Bean
  CommandLineRunner initDuckDb(DataSource dataSource) {
    return args -> {
      Files.createDirectories(PARQUET_DIR);

      boolean hasParquet = false;
      try (Stream<Path> s = Files.list(PARQUET_DIR)) {
        hasParquet = s.anyMatch(p -> p.toString().endsWith(".parquet"));
      }

      try (Connection conn = dataSource.getConnection();
           Statement stmt = conn.createStatement()) {

        if (hasParquet) {
          stmt.execute("""
            CREATE OR REPLACE VIEW awards AS
            SELECT * FROM read_parquet('storage/parquet/awards/*.parquet');
          """);
          System.out.println("DuckDB view 'awards' ready (parquet attached)");
        } else {
          // empty placeholder so the app can start before first ingestion
          stmt.execute("""
            CREATE OR REPLACE TABLE awards (
              award_id VARCHAR,
              recipient_name VARCHAR,
              start_date DATE,
              end_date DATE,
              award_amount DOUBLE,
              awarding_agency VARCHAR,
              awarding_sub_agency VARCHAR,
              is_active BOOLEAN,
              months_to_expiration INTEGER
            );
          """);
          System.out.println("DuckDB table 'awards' ready (empty placeholder)");
        }
      }
    };
  }
}

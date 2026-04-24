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
  CommandLineRunner initDuckDb(DataSource dataSource, SqlTemplates sql) {
    return args -> {
      Files.createDirectories(PARQUET_DIR);

      boolean hasParquet;
      try (Stream<Path> s = Files.list(PARQUET_DIR)) {
        hasParquet = s.anyMatch(p -> p.toString().endsWith(".parquet"));
      }

      try (Connection conn = dataSource.getConnection();
           Statement stmt = conn.createStatement()) {

          if (hasParquet) {
              stmt.execute("""
                  CREATE OR REPLACE VIEW contract_awards AS
                  SELECT * FROM read_parquet('storage/parquet/awards/*.parquet');
              """);
              System.out.println("DuckDB view 'contract_awards' ready (parquet attached)");
          } else {
              stmt.execute("""
                  CREATE OR REPLACE VIEW contract_awards AS
                  SELECT
                      NULL::VARCHAR AS award_id,
                      NULL::VARCHAR AS recipient_name,
                      NULL::DATE AS start_date,
                      NULL::DATE AS end_date,
                      NULL::DOUBLE AS award_amount,
                      NULL::VARCHAR AS awarding_agency,
                      NULL::VARCHAR AS awarding_sub_agency,
                      NULL::BOOLEAN AS is_active,
                      NULL::BIGINT AS months_to_expiration
                  WHERE FALSE;
              """);
          
              System.out.println("DuckDB empty placeholder view 'contract_awards' ready");
          }

        // 2) Derived analytics views (your 3 SQL files)
        stmt.execute(sql.expiringContractsQuery());
        stmt.execute(sql.partnerProfileQuery());
        stmt.execute(sql.partnerSearchQuery());

        System.out.println("DuckDB analytics views ready (expiring_contracts, partner_profile, partner_customer_experience)");
      }
    };
  }
}

package com.example.contract_master.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class DuckDbConfig {

  @Bean
  public DataSource dataSource() {
    DriverManagerDataSource ds = new DriverManagerDataSource();
    ds.setDriverClassName("org.duckdb.DuckDBDriver");

    // File-backed DuckDB so every connection sees the same tables/views
    ds.setUrl("jdbc:duckdb:storage/duckdb/contract_master.duckdb");

    return ds;
  }
}

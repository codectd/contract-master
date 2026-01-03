package com.example.contract_master.analytics;

import java.nio.charset.StandardCharsets;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class SqlTemplates {

  public String expiringContracts() {
    return read("sql/expiring_contracts.sql");
  }

  private String read(String path) {
    try {
      var res = new ClassPathResource(path);
      try (var in = res.getInputStream()) {
        return new String(in.readAllBytes(), StandardCharsets.UTF_8);
      }
    } catch (Exception e) {
      throw new RuntimeException("Failed to read SQL template: " + path, e);
    }
  }
}

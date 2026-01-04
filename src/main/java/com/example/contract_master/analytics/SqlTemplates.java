package com.example.contract_master.analytics;

import java.nio.charset.StandardCharsets;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class SqlTemplates {

    public String expiringContractsQuery() {
        return read("sql/expiring_contracts.sql");
      }
      
      public String partnerProfileQuery() {
        return read("sql/partner_profile.sql");
      }
      
      public String partnerSearchQuery() {
        return read("sql/partner_search.sql");
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

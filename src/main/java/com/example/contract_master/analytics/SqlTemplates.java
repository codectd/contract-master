package com.example.contract_master.analytics;

import java.nio.charset.StandardCharsets;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class SqlTemplates {

    public String expiringContractsQuery() {
        return read("sql/views/expiring_contracts_view.sql");
      }
      
      public String partnerProfileQuery() {
        return read("sql/views/partner_profile_view.sql");
      }
      
      public String partnerSearchQuery() {
        return read("sql/views/partner_search_view.sql");
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

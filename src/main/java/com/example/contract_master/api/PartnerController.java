package com.example.contract_master.api;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.contract_master.analytics.AnalyticsRepository;
import com.example.contract_master.api.dto.PartnerProfileDto;
import com.example.contract_master.api.dto.PartnerSearchRowDto;

@RestController
@RequestMapping("/api/partners")
public class PartnerController {

  private final AnalyticsRepository repo;

  public PartnerController(AnalyticsRepository repo) {
    this.repo = repo;
  }

  // Example: /api/partners/profile?name=ACME&limit=25&offset=0
  @GetMapping("/profile")
  public List<PartnerProfileDto> profile(
      @RequestParam String name,
      @RequestParam(defaultValue = "25") int limit,
      @RequestParam(defaultValue = "0") int offset
  ) {
    return repo.findPartnerProfiles(name, limit, offset);
  }

  // Example: /api/partners/search?agency=VA&limit=25&offset=0
  @GetMapping("/search")
  public List<PartnerSearchRowDto> search(
      @RequestParam String agency,
      @RequestParam(defaultValue = "25") int limit,
      @RequestParam(defaultValue = "0") int offset
  ) {
    return repo.searchPartnersByCustomer(agency, limit, offset);
  }
}

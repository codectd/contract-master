package com.example.contract_master.api;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.contract_master.analytics.AnalyticsRepository;
import com.example.contract_master.api.dto.ExpiringContractDto;

@RestController
@RequestMapping("/api/contracts")
public class ContractController {

  private final AnalyticsRepository repo;

  public ContractController(AnalyticsRepository repo) {
    this.repo = repo;
  }

  // Example:
  // /api/contracts/expiring?minMonths=6&maxMonths=24&agency=VA&limit=100&offset=0
  @GetMapping("/expiring")
  public List<ExpiringContractDto> expiring(
      @RequestParam(defaultValue = "6") int minMonths,
      @RequestParam(defaultValue = "24") int maxMonths,
      @RequestParam(required = false) String agency,
      @RequestParam(defaultValue = "100") int limit,
      @RequestParam(defaultValue = "0") int offset
  ) {
    return repo.findExpiringContracts(
        minMonths,
        maxMonths,
        agency,
        limit,
        offset
    );
  }
}

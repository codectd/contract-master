package com.example.contract_master.api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
  private static final Logger log = LoggerFactory.getLogger(ContractController.class);

  public ContractController(AnalyticsRepository repo) {
    this.repo = repo;
  }
  // likely_recompete = true
  // months_to_expiration <= 18
  // award_amount >= threshold
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
    log.info("/expiring contracts returned");
    return repo.findExpiringContracts(
        minMonths,
        maxMonths,
        agency,
        limit,
        offset
    );
  }
}

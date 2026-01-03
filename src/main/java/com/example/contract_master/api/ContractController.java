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

  @GetMapping("/expiring")
  public List<ExpiringContractDto> expiring(
      @RequestParam(defaultValue = "6") Integer minMonths,
      @RequestParam(defaultValue = "24") Integer maxMonths,
      @RequestParam(required = false) String agency
  ) {
    return repo.findExpiringContracts(minMonths, maxMonths, agency);
  }
}

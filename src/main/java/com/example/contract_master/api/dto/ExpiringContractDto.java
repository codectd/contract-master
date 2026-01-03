package com.example.contract_master.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpiringContractDto(
    String awardId,
    String recipientName,
    String awardingAgency,
    BigDecimal awardAmount,
    LocalDate endDate,
    Integer monthsToExpiration
) {}

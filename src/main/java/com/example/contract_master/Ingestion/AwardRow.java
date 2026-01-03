package com.example.contract_master.Ingestion;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AwardRow(
    String awardId,
    String recipientName,
    LocalDate startDate,
    LocalDate endDate,
    BigDecimal awardAmount,
    String awardingAgency,
    String awardingSubAgency,
    boolean isActive,
    Integer monthsToExpiration
) {}


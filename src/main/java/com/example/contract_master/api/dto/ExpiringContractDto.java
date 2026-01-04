package com.example.contract_master.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpiringContractDto(
    String awardId,
    String recipientName,
    String recipientUei,
    String awardingAgency,
    String subAgency,
    String naics,
    String contractVehicle,
    BigDecimal awardAmount,
    BigDecimal potentialTotalAmount,
    LocalDate endDate,
    Integer monthsToExpiration,
    boolean likelyRecompete
) {}


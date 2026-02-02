package com.example.contract_master.api.dto;

import java.time.LocalDate;

public record ExpiringContractDto(
    String awardId,
    String recipientName,
    String awardingAgency,
    String awardingSubAgency,
    double awardAmount,
    LocalDate startDate,
    LocalDate endDate,
    long monthsToExpiration,
    boolean active
) {}



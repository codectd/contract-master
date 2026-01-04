package com.example.contract_master.api.dto;

import java.math.BigDecimal;

public record PartnerProfileDto(
    String recipientUei,
    String recipientName,
    long totalContracts,
    BigDecimal totalCeiling,
    long agenciesServed,
    long vehiclesUsed
) {}

package com.example.contract_master.api.dto;

import java.math.BigDecimal;

public record PartnerSearchRowDto(
    String awardingAgency,
    String recipientUei,
    String recipientName,
    long contractsWon,
    BigDecimal totalCeiling
) {}

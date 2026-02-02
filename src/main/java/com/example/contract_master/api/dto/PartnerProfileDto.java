package com.example.contract_master.api.dto;

import java.util.List;

public record PartnerProfileDto(
    String recipientName,
    long totalContracts,
    double totalAwardAmount,
    List<String> agenciesServed
) {}


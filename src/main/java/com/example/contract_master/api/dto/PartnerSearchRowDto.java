package com.example.contract_master.api.dto;

public record PartnerSearchRowDto(
    String awardingAgency,
    String recipientName,
    long contractsWon,
    double totalAwardAmount
) {}


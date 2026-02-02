package com.example.contract_master.analytics;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.contract_master.api.dto.ExpiringContractDto;
import com.example.contract_master.api.dto.PartnerProfileDto;
import com.example.contract_master.api.dto.PartnerSearchRowDto;

@Repository
public class AnalyticsRepository {

    private final JdbcTemplate jdbc;

    public AnalyticsRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<ExpiringContractDto> findExpiringContracts(
        int minMonths,
        int maxMonths,
        String agency,
        int limit,
        int offset
    ) {
        return jdbc.query(
            """
            SELECT *
            FROM expiring_contracts
            WHERE months_to_expiration BETWEEN ? AND ?
              AND (? IS NULL OR awarding_agency = ?)
            ORDER BY months_to_expiration ASC, award_amount DESC
            LIMIT ? OFFSET ?
            """,
            (rs, i) -> new ExpiringContractDto(
                rs.getString("award_id"),
                rs.getString("recipient_name"),
                rs.getString("awarding_agency"),
                rs.getString("awarding_sub_agency"),
                rs.getDouble("award_amount"),
                rs.getDate("start_date").toLocalDate(),
                rs.getDate("end_date") == null ? null : rs.getDate("end_date").toLocalDate(),
                rs.getLong("months_to_expiration"),
                rs.getBoolean("is_active")
            ),
            minMonths, maxMonths, agency, agency, limit, offset
        );
    }

    public List<PartnerProfileDto> findPartnerProfiles(int limit, int offset) {
        return jdbc.query(
            """
            SELECT *
            FROM partner_profile
            ORDER BY total_contracts DESC
            LIMIT ? OFFSET ?
            """,
            (rs, i) -> new PartnerProfileDto(
                rs.getString("recipient_name"),
                rs.getLong("total_contracts"),
                rs.getDouble("total_award_amount"),
                (List<String>) rs.getObject("agencies_served")
            ),
            limit, offset
        );
    }

    public List<PartnerSearchRowDto> searchPartnersByCustomer(
        String agency,
        int limit,
        int offset
    ) {
        return jdbc.query(
            """
            SELECT *
            FROM partner_customer_experience
            WHERE awarding_agency = ?
            ORDER BY contracts_won DESC
            LIMIT ? OFFSET ?
            """,
            (rs, i) -> new PartnerSearchRowDto(
                rs.getString("awarding_agency"),
                rs.getString("recipient_name"),
                rs.getLong("contracts_won"),
                rs.getDouble("total_award_amount")
            ),
            agency, limit, offset
        );
    }
}

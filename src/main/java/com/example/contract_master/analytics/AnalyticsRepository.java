package com.example.contract_master.analytics;

import java.util.List;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.contract_master.api.dto.ExpiringContractDto;

@Repository
public class AnalyticsRepository {

  private final NamedParameterJdbcTemplate jdbc;
  private final SqlTemplates sql;

  public AnalyticsRepository(NamedParameterJdbcTemplate jdbc, SqlTemplates sql) {
    this.jdbc = jdbc;
    this.sql = sql;
  }

  public List<ExpiringContractDto> findExpiringContracts(Integer minMonths, Integer maxMonths, String agency) {

  var params = new java.util.HashMap<String, Object>();
  params.put("minMonths", minMonths);
  params.put("maxMonths", maxMonths);
  params.put("agency", agency); // OK even if null

  return jdbc.query(
      sql.expiringContracts(),
      params,
      (rs, rowNum) -> new ExpiringContractDto(
          rs.getString("award_id"),
          rs.getString("recipient_name"),
          rs.getString("awarding_agency"),
          rs.getBigDecimal("award_amount"),
          rs.getDate("end_date") == null ? null : rs.getDate("end_date").toLocalDate(),
          rs.getObject("months_to_expiration") == null ? null : rs.getInt("months_to_expiration")
      )
  );
}

}

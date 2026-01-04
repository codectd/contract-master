package com.example.contract_master.analytics;

import java.util.List;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.contract_master.api.dto.ExpiringContractDto;
import com.example.contract_master.api.dto.PartnerProfileDto;
import com.example.contract_master.api.dto.PartnerSearchRowDto;

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

    return jdbc.query(sql.expiringContractsQuery(), params, (rs, i) ->
    new ExpiringContractDto(
        rs.getString("award_id"),
        rs.getString("recipient_name"),
        rs.getString("recipient_uei"),
        rs.getString("awarding_agency"),
        rs.getString("sub_agency"),
        rs.getString("naics"),
        rs.getString("contract_vehicle"),
        rs.getBigDecimal("award_amount"),
        rs.getBigDecimal("potential_total_amount"),
        rs.getDate("end_date") == null ? null : rs.getDate("end_date").toLocalDate(),
        (Integer) rs.getObject("months_to_expiration"),
        rs.getObject("likely_recompete") != null && rs.getBoolean("likely_recompete")
    )
);

  }

  public List<ExpiringContractDto> getExpiringContracts(String agency, String naics, String vehicle, int limit, int offset) {
  var params = new java.util.HashMap<String, Object>();
  params.put("agency", agency);
  params.put("naics", naics);
  params.put("vehicle", vehicle);
  params.put("limit", limit);
  params.put("offset", offset);

  return jdbc.query(sql.expiringContractsQuery(), params, (rs, i) ->
    new ExpiringContractDto(
        rs.getString("award_id"),
        rs.getString("recipient_name"),
        rs.getString("recipient_uei"),
        rs.getString("awarding_agency"),
        rs.getString("sub_agency"),
        rs.getString("naics"),
        rs.getString("contract_vehicle"),
        rs.getBigDecimal("award_amount"),
        rs.getBigDecimal("potential_total_amount"),
        rs.getDate("end_date") == null ? null : rs.getDate("end_date").toLocalDate(),
        (Integer) rs.getObject("months_to_expiration"),
        rs.getObject("likely_recompete") != null && rs.getBoolean("likely_recompete")
    )
);

}

public List<PartnerProfileDto> findPartnerProfiles(String name, int limit, int offset) {
  var params = new java.util.HashMap<String, Object>();
  params.put("nameLike", "%" + name + "%");
  params.put("limit", limit);
  params.put("offset", offset);

  return jdbc.query(sql.partnerProfileQuery(), params, (rs, i) ->
      new PartnerProfileDto(
          rs.getString("recipient_uei"),
          rs.getString("recipient_name"),
          rs.getLong("total_contracts"),
          rs.getBigDecimal("total_ceiling"),
          rs.getLong("agencies_served"),
          rs.getLong("vehicles_used")
      )
  );
}

public List<PartnerSearchRowDto> searchPartnersByCustomer(String agency, int limit, int offset) {
  var params = new java.util.HashMap<String, Object>();
  params.put("agency", agency);
  params.put("limit", limit);
  params.put("offset", offset);

  return jdbc.query(sql.partnerSearchQuery(), params, (rs, i) ->
      new PartnerSearchRowDto(
          rs.getString("awarding_agency"),
          rs.getString("recipient_uei"),
          rs.getString("recipient_name"),
          rs.getLong("contracts_won"),
          rs.getBigDecimal("total_ceiling")
      )
  );
}


}

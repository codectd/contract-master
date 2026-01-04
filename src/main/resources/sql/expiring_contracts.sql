SELECT
  award_id,
  recipient_name,
  recipient_uei,
  awarding_agency,
  sub_agency,
  naics,
  vehicle_normalized AS contract_vehicle,
  potential_total_amount,
  award_amount,
  end_date,
  months_to_expiration
FROM expiring_contracts
WHERE awarding_agency = COALESCE(:agency, awarding_agency)
  AND naics = COALESCE(:naics, naics)
  AND vehicle_normalized = COALESCE(:vehicle, vehicle_normalized)
ORDER BY months_to_expiration ASC, potential_total_amount DESC
LIMIT :limit OFFSET :offset;

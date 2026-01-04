CREATE VIEW expiring_contracts AS
SELECT
  award_id,
  recipient_name,
  recipient_uei,
  awarding_agency,
  sub_agency,
  naics,
  vehicle_normalized AS contract_vehicle,
  award_amount,
  potential_total_amount,
  end_date,
  months_to_expiration,
  likely_recompete
FROM contract_awards
WHERE is_active = true
  AND months_to_expiration BETWEEN 6 AND 24;


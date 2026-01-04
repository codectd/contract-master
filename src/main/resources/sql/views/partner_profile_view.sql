CREATE OR REPLACE VIEW partner_profile AS
SELECT
  recipient_uei,
  recipient_name,
  COUNT(*) AS total_contracts,
  SUM(potential_total_amount) AS total_ceiling,
  COUNT(DISTINCT awarding_agency) AS agencies_served,
  COUNT(DISTINCT vehicle_normalized) AS vehicles_used
FROM contract_awards
WHERE is_prime = true
GROUP BY recipient_uei, recipient_name;
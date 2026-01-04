CREATE OR REPLACE VIEW partner_customer_experience AS
SELECT
  awarding_agency,
  recipient_uei,
  recipient_name,
  COUNT(*) AS contracts_won,
  SUM(potential_total_amount) AS total_ceiling
FROM contract_awards
WHERE is_prime = true
GROUP BY awarding_agency, recipient_uei, recipient_name;
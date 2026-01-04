SELECT
  awarding_agency,
  recipient_uei,
  recipient_name,
  contracts_won,
  total_ceiling
FROM partner_customer_experience
WHERE awarding_agency = COALESCE(:agency, awarding_agency)
ORDER BY contracts_won DESC, total_ceiling DESC
LIMIT :limit OFFSET :offset;

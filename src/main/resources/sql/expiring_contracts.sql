SELECT
  award_id,
  recipient_name,
  awarding_agency,
  award_amount,
  end_date,
  months_to_expiration
FROM awards
WHERE months_to_expiration BETWEEN :minMonths AND :maxMonths
  AND awarding_agency = COALESCE(:agency, awarding_agency)
ORDER BY months_to_expiration ASC;

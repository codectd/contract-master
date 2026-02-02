CREATE OR REPLACE VIEW partner_customer_experience AS
SELECT
  awarding_agency,
  recipient_name,
  COUNT(*) AS contracts_won,
  SUM(award_amount) AS total_award_amount
FROM contract_awards
GROUP BY awarding_agency, recipient_name;

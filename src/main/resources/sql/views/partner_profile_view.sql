CREATE OR REPLACE VIEW partner_search AS
SELECT
  recipient_name,
  COUNT(*) AS total_contracts,
  SUM(award_amount) AS total_award_amount,
  LIST(DISTINCT awarding_agency) AS agencies_served
FROM contract_awards
GROUP BY recipient_name;

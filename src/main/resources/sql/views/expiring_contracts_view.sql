CREATE OR REPLACE VIEW expiring_contracts AS
SELECT
  award_id,
  recipient_name,
  awarding_agency,
  awarding_sub_agency,
  award_amount,
  start_date,
  end_date,
  months_to_expiration,
  is_active
FROM contract_awards;

SELECT
  recipient_uei,
  recipient_name,
  total_contracts,
  total_ceiling,
  agencies_served,
  vehicles_used
FROM partner_profile
WHERE LOWER(recipient_name) LIKE LOWER(:nameLike)
ORDER BY total_ceiling DESC
LIMIT :limit OFFSET :offset;

package com.example.contract_master.Ingestion;

import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

@Component
public class AwardNormalizer {

  public List<AwardRow> normalize(JsonNode results) {
    List<AwardRow> rows = new ArrayList<>();
    for (JsonNode r : results) {
      String awardId = text(r, "Award ID");
      String recipient = text(r, "Recipient Name");
      String awardingAgency = text(r, "Awarding Agency");
      String awardingSub = text(r, "Awarding Sub Agency");

      LocalDate start = date(r, "Start Date");
      LocalDate end = date(r, "End Date");

      BigDecimal amount = decimal(r, "Award Amount");

      boolean active = end != null && end.isAfter(LocalDate.now());
      Integer monthsToExp = end == null ? null : monthsBetween(LocalDate.now(), end);

      rows.add(new AwardRow(
          awardId, recipient, start, end, amount,
          awardingAgency, awardingSub, active, monthsToExp
      ));
    }
    return rows;
  }

  public Path writeNormalizedCsv(List<AwardRow> rows, Path outFile) throws IOException {
    Files.createDirectories(outFile.getParent());
    try (BufferedWriter w = Files.newBufferedWriter(outFile, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
      w.write("award_id,recipient_name,start_date,end_date,award_amount,awarding_agency,awarding_sub_agency,is_active,months_to_expiration\n");
      for (AwardRow row : rows) {
        w.write(csv(row.awardId())); w.write(",");
        w.write(csv(row.recipientName())); w.write(",");
        w.write(csv(row.startDate() == null ? "" : row.startDate().toString())); w.write(",");
        w.write(csv(row.endDate() == null ? "" : row.endDate().toString())); w.write(",");
        w.write(csv(row.awardAmount() == null ? "" : row.awardAmount().toPlainString())); w.write(",");
        w.write(csv(row.awardingAgency())); w.write(",");
        w.write(csv(row.awardingSubAgency())); w.write(",");
        w.write(row.isActive() ? "true" : "false"); w.write(",");
        w.write(row.monthsToExpiration() == null ? "" : row.monthsToExpiration().toString());
        w.write("\n");
      }
    }
    return outFile;
  }

  private static String text(JsonNode node, String field) {
    JsonNode v = node.get(field);
    return v == null || v.isNull() ? "" : v.asText("");
  }

  private static LocalDate date(JsonNode node, String field) {
    String s = text(node, field);
    if (s == null || s.isBlank()) return null;
    try { return LocalDate.parse(s); } catch (Exception e) { return null; }
  }

  private static BigDecimal decimal(JsonNode node, String field) {
    String s = text(node, field);
    if (s == null || s.isBlank()) return null;
    try { return new BigDecimal(s); } catch (Exception e) { return null; }
  }

  private static Integer monthsBetween(LocalDate a, LocalDate b) {
    if (b.isBefore(a)) return 0;
    Period p = Period.between(a.withDayOfMonth(1), b.withDayOfMonth(1));
    return p.getYears() * 12 + p.getMonths();
  }

  private static String csv(String s) {
    if (s == null) return "";
    String escaped = s.replace("\"", "\"\"");
    return "\"" + escaped + "\"";
  }
}

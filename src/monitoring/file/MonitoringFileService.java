package monitoring.file;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.Gson;

import monitoring.model.MatchSummary;
import monitoring.model.MonitoringDataType;
import monitoring.model.MonitoringRecord;
import monitoring.model.TimeRange;

// Reusable file service for monitoring-style text files and Gson-based JSON IO.
public class MonitoringFileService {
    private static final DateTimeFormatter HOUR_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHH");
    private final Gson gson;

    public MonitoringFileService() {
        this.gson = new Gson();
    }

    // Reads raw text lines without applying any parsing rule.
    public List<String> readLines(Path path) throws IOException {
        return Files.readAllLines(path, StandardCharsets.UTF_8);
    }

    // Reads a file and parses each non-empty line using the provided parser.
    public <T> List<T> readParsedLines(Path path, LineParser<T> parser) throws IOException {
        return parseLines(readLines(path), parser);
    }

    // Reuses the same parsing flow for lines that are already loaded in memory.
    public <T> List<T> parseLines(List<String> lines, LineParser<T> parser) {
        List<T> parsedItems = new ArrayList<T>();

        for (int index = 0; index < lines.size(); index++) {
            String line = lines.get(index);
            String trimmed = line.trim();
            if (trimmed.isEmpty()) {
                continue;
            }
            parsedItems.add(parser.parse(trimmed, index + 1));
        }

        return parsedItems;
    }

    // Convenience parser for simple "key value" style files.
    public Map<String, String> readKeyValueFile(Path path) throws IOException {
        Map<String, String> parsed = new LinkedHashMap<String, String>();
        List<Map.Entry<String, String>> entries = readParsedLines(path, new LineParser<Map.Entry<String, String>>() {
            @Override
            public Map.Entry<String, String> parse(String line, int lineNumber) {
                return parseKeyValueLine(line, lineNumber);
            }
        });

        for (Map.Entry<String, String> entry : entries) {
            parsed.put(entry.getKey(), entry.getValue());
        }

        return parsed;
    }

    // Reads the project's default monitoring format: reqId#yyyyMMddHH#type#value.
    public List<MonitoringRecord> readMonitoringRecords(Path path) throws IOException {
        return readParsedLines(path, new LineParser<MonitoringRecord>() {
            @Override
            public MonitoringRecord parse(String line, int lineNumber) {
                return parseMonitoringRecord(line, lineNumber);
            }
        });
    }

    // Parses one monitoring record line into a typed object.
    public MonitoringRecord parseMonitoringRecord(String line, int lineNumber) {
        String[] tokens = line.split("#", -1);
        if (tokens.length != 4) {
            throw new IllegalArgumentException("Invalid record format at line " + lineNumber + ": " + line);
        }

        return new MonitoringRecord(
                tokens[0].trim(),
                parseHourTimestamp(tokens[1].trim()),
                MonitoringDataType.from(tokens[2]),
                tokens[3].trim());
    }

    // Parses one "key value" line with line number aware validation.
    public Map.Entry<String, String> parseKeyValueLine(String line, int lineNumber) {
        String[] tokens = line.split("\\s+", 2);
        if (tokens.length != 2) {
            throw new IllegalArgumentException("Invalid key-value line at line " + lineNumber + ": " + line);
        }
        return new AbstractMap.SimpleEntry<String, String>(tokens[0], tokens[1]);
    }

    // Compares the latest P and A value for each request id and builds a summary.
    public MatchSummary calculateMatchSummary(List<MonitoringRecord> records) {
        Map<String, Map<MonitoringDataType, MonitoringRecord>> latestByRequestId =
                new HashMap<String, Map<MonitoringDataType, MonitoringRecord>>();

        for (MonitoringRecord record : records) {
            Map<MonitoringDataType, MonitoringRecord> grouped = latestByRequestId.get(record.getRequestId());
            if (grouped == null) {
                grouped = new LinkedHashMap<MonitoringDataType, MonitoringRecord>();
                latestByRequestId.put(record.getRequestId(), grouped);
            }

            MonitoringRecord current = grouped.get(record.getDataType());
            if (current == null || current.getTimestamp().isBefore(record.getTimestamp())) {
                grouped.put(record.getDataType(), record);
            }
        }

        int matchedCount = 0;
        int unmatchedCount = 0;

        for (Map<MonitoringDataType, MonitoringRecord> grouped : latestByRequestId.values()) {
            MonitoringRecord monitoring = grouped.get(MonitoringDataType.P);
            MonitoringRecord prediction = grouped.get(MonitoringDataType.A);

            if (monitoring == null || prediction == null) {
                continue;
            }

            if (monitoring.getValue().equals(prediction.getValue())) {
                matchedCount++;
            } else {
                unmatchedCount++;
            }
        }

        return new MatchSummary(matchedCount + unmatchedCount, matchedCount, unmatchedCount);
    }

    // Filters records by both data type and time range.
    public List<MonitoringRecord> filterByTypeAndRange(
            List<MonitoringRecord> records,
            MonitoringDataType dataType,
            TimeRange range) {
        return records.stream()
                .filter(record -> record.getDataType() == dataType)
                .filter(record -> range.contains(record.getTimestamp()))
                .sorted(Comparator.comparing(MonitoringRecord::getTimestamp))
                .collect(Collectors.toList());
    }

    // Filters records only by time range, keeping both P and A types.
    public List<MonitoringRecord> filterByRange(List<MonitoringRecord> records, TimeRange range) {
        return records.stream()
                .filter(record -> range.contains(record.getTimestamp()))
                .sorted(Comparator.comparing(MonitoringRecord::getTimestamp))
                .collect(Collectors.toList());
    }

    // Builds a monthly range from month start to the target hour's 59:59.
    public TimeRange createMonthlyRangeFromHour(String yyyyMMddHH) {
        LocalDateTime target = parseHourTimestamp(yyyyMMddHH);
        YearMonth yearMonth = YearMonth.from(target);
        LocalDateTime start = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime end = LocalDate.of(target.getYear(), target.getMonth(), target.getDayOfMonth())
                .atTime(target.getHour(), 59, 59);
        return new TimeRange(start, end);
    }

    // Shortcut for "monitoring only" monthly queries.
    public List<MonitoringRecord> findMonitoringRecordsForMonthlyRange(List<MonitoringRecord> records, String yyyyMMddHH) {
        TimeRange range = createMonthlyRangeFromHour(yyyyMMddHH);
        return filterByTypeAndRange(records, MonitoringDataType.P, range);
    }

    // Builds a direct time window from explicit start and end hour strings.
    public TimeRange createRangeFromHours(String startHour, String endHour) {
        LocalDateTime start = parseHourTimestamp(startHour).withMinute(0).withSecond(0);
        LocalDateTime end = parseHourTimestamp(endHour).withMinute(59).withSecond(59);

        if (end.isBefore(start)) {
            throw new IllegalArgumentException("endHour must be after or equal to startHour.");
        }

        return new TimeRange(start, end);
    }

    // Groups records by request id for request-centered inspection or post-processing.
    public Map<String, List<MonitoringRecord>> groupByRequestId(List<MonitoringRecord> records) {
        return records.stream().collect(Collectors.groupingBy(
                MonitoringRecord::getRequestId,
                LinkedHashMap::new,
                Collectors.toList()));
    }

    // Writes plain text lines back to a file.
    public void writeLines(Path path, List<String> lines) throws IOException {
        Files.write(path, lines, StandardCharsets.UTF_8);
    }

    // Reads a JSON file into a Java object using Gson.
    public <T> T readJson(Path path, Class<T> type) throws IOException {
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            return gson.fromJson(reader, type);
        }
    }

    // Writes a Java object as JSON.
    public void writeJson(Path path, Object value) throws IOException {
        try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            gson.toJson(value, writer);
        }
    }

    // Converts an object to a JSON string for logging or HTTP usage.
    public String toJson(Object value) {
        return gson.toJson(value);
    }

    private LocalDateTime parseHourTimestamp(String value) {
        try {
            return LocalDateTime.parse(value, HOUR_FORMATTER);
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException("Invalid timestamp format (expected yyyyMMddHH): " + value, exception);
        }
    }
}

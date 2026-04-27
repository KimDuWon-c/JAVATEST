package FileIO;

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

public class fileInput {
    private static final DateTimeFormatter HOUR_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHH");
    private final Gson gson;

    public fileInput() {
        this.gson = new Gson();
    }

    public List<String> readLines(Path path) throws IOException {
        return Files.readAllLines(path, StandardCharsets.UTF_8);
    }

    public <T> List<T> readParsedLines(Path path, LineParser<T> parser) throws IOException {
        return parseLines(readLines(path), parser);
    }

    public <T> List<T> parseLines(List<String> lines, LineParser<T> parser) {
        List<T> parsedItems = new ArrayList<>();

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

    public Map<String, String> readKeyValueFile(Path path) throws IOException {
        Map<String, String> parsed = new LinkedHashMap<>();
        List<Map.Entry<String, String>> entries = readParsedLines(path, this::parseKeyValueLine);

        for (Map.Entry<String, String> entry : entries) {
            parsed.put(entry.getKey(), entry.getValue());
        }

        return parsed;
    }

    public List<FileRecord> readPredictionRecords(Path path) throws IOException {
        return readParsedLines(path, this::parsePredictionRecord);
    }

    public FileRecord parsePredictionRecord(String line, int lineNumber) {
        String[] tokens = line.split("#", -1);
        if (tokens.length != 4) {
            throw new IllegalArgumentException("Invalid record format at line " + lineNumber + ": " + line);
        }

        return new FileRecord(
                tokens[0].trim(),
                parseHourTimestamp(tokens[1].trim()),
                DataType.from(tokens[2]),
                tokens[3].trim());
    }

    public Map.Entry<String, String> parseKeyValueLine(String line, int lineNumber) {
        String[] tokens = line.split("\\s+", 2);
        if (tokens.length != 2) {
            throw new IllegalArgumentException("Invalid key-value line at line " + lineNumber + ": " + line);
        }

        return new AbstractMap.SimpleEntry<String, String>(tokens[0], tokens[1]);
    }

    public MatchSummary calculateMatchSummary(List<FileRecord> records) {
        Map<String, Map<DataType, FileRecord>> latestByRequestId = new HashMap<>();

        for (FileRecord record : records) {
            Map<DataType, FileRecord> grouped = latestByRequestId.computeIfAbsent(
                    record.getRequestId(),
                    key -> new LinkedHashMap<>());

            FileRecord current = grouped.get(record.getDataType());
            if (current == null || current.getTimestamp().isBefore(record.getTimestamp())) {
                grouped.put(record.getDataType(), record);
            }
        }

        int matchedCount = 0;
        int unmatchedCount = 0;

        for (Map<DataType, FileRecord> grouped : latestByRequestId.values()) {
            FileRecord monitoring = grouped.get(DataType.P);
            FileRecord prediction = grouped.get(DataType.A);

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

    public List<FileRecord> filterByTypeAndRange(List<FileRecord> records, DataType dataType, DateTimeRange range) {
        return records.stream()
                .filter(record -> record.getDataType() == dataType)
                .filter(record -> range.contains(record.getTimestamp()))
                .sorted(Comparator.comparing(FileRecord::getTimestamp))
                .collect(Collectors.toList());
    }

    public List<FileRecord> filterByRange(List<FileRecord> records, DateTimeRange range) {
        return records.stream()
                .filter(record -> range.contains(record.getTimestamp()))
                .sorted(Comparator.comparing(FileRecord::getTimestamp))
                .collect(Collectors.toList());
    }

    public DateTimeRange createMonthlyRangeFromHour(String yyyyMMddHH) {
        LocalDateTime target = parseHourTimestamp(yyyyMMddHH);
        YearMonth yearMonth = YearMonth.from(target);
        LocalDateTime start = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime end = LocalDate.of(target.getYear(), target.getMonth(), target.getDayOfMonth())
                .atTime(target.getHour(), 59, 59);
        return new DateTimeRange(start, end);
    }

    public List<FileRecord> findMonitoringRecordsForMonthlyRange(List<FileRecord> records, String yyyyMMddHH) {
        DateTimeRange range = createMonthlyRangeFromHour(yyyyMMddHH);
        return filterByTypeAndRange(records, DataType.P, range);
    }

    public DateTimeRange createRangeFromHours(String startHour, String endHour) {
        LocalDateTime start = parseHourTimestamp(startHour).withMinute(0).withSecond(0);
        LocalDateTime end = parseHourTimestamp(endHour).withMinute(59).withSecond(59);

        if (end.isBefore(start)) {
            throw new IllegalArgumentException("endHour must be after or equal to startHour.");
        }

        return new DateTimeRange(start, end);
    }

    public Map<String, List<FileRecord>> groupByRequestId(List<FileRecord> records) {
        return records.stream().collect(Collectors.groupingBy(
                FileRecord::getRequestId,
                LinkedHashMap::new,
                Collectors.toList()));
    }

    public void writeLines(Path path, List<String> lines) throws IOException {
        Files.write(path, lines, StandardCharsets.UTF_8);
    }

    public <T> T readJson(Path path, Class<T> type) throws IOException {
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            return gson.fromJson(reader, type);
        }
    }

    public void writeJson(Path path, Object value) throws IOException {
        try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            gson.toJson(value, writer);
        }
    }

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

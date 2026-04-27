package purejava.file;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TextFileService {
    public List<String> readLines(Path path) throws IOException {
        return Files.readAllLines(path, StandardCharsets.UTF_8);
    }

    public Map<String, String> readKeyValuePairs(Path path) throws IOException {
        List<String> lines = readLines(path);
        Map<String, String> result = new LinkedHashMap<>();

        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.isEmpty()) {
                continue;
            }

            String[] tokens = trimmed.split("\\s+", 2);
            if (tokens.length != 2) {
                throw new IllegalArgumentException("Invalid line format: " + line);
            }

            result.put(tokens[0], tokens[1]);
        }

        return result;
    }

    public void writeLines(Path path, List<String> lines) throws IOException {
        Files.write(path, lines, StandardCharsets.UTF_8);
    }
}

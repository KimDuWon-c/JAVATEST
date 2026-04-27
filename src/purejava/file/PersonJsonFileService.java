package purejava.file;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import purejava.model.Person;

public class PersonJsonFileService {
    private static final Pattern NAME_PATTERN = Pattern.compile("\"name\"\\s*:\\s*\"([^\"]*)\"");
    private static final Pattern AGE_PATTERN = Pattern.compile("\"age\"\\s*:\\s*(\\d+)");

    public Person read(Path path) throws IOException {
        String json = Files.readString(path, StandardCharsets.UTF_8);
        String name = findRequired(json, NAME_PATTERN, "name");
        int age = Integer.parseInt(findRequired(json, AGE_PATTERN, "age"));
        return new Person(name, age);
    }

    public void write(Path path, Person person) throws IOException {
        Files.writeString(path, person.toJson(), StandardCharsets.UTF_8);
    }

    private String findRequired(String json, Pattern pattern, String fieldName) {
        Matcher matcher = pattern.matcher(json);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Missing JSON field: " + fieldName);
        }
        return matcher.group(1);
    }
}

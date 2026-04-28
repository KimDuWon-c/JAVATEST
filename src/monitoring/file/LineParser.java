package monitoring.file;

// Strategy interface for converting one text line into one typed object.
public interface LineParser<T> {
    T parse(String line, int lineNumber);
}

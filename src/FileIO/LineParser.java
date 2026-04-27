package FileIO;

@FunctionalInterface
public interface LineParser<T> {
    T parse(String line, int lineNumber);
}

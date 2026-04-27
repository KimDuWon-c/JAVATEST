package FileIO;

public enum DataType {
    P,
    A;

    public static DataType from(String value) {
        return DataType.valueOf(value.trim().toUpperCase());
    }
}

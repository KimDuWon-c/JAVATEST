package monitoring.model;

public enum MonitoringDataType {
    P,
    A;

    public static MonitoringDataType from(String value) {
        return MonitoringDataType.valueOf(value.trim().toUpperCase());
    }
}

package monitoring.model;

import java.time.LocalDateTime;

public class MonitoringRecord {
    private String requestId;
    private LocalDateTime timestamp;
    private MonitoringDataType dataType;
    private String value;

    public MonitoringRecord() {
    }

    public MonitoringRecord(String requestId, LocalDateTime timestamp, MonitoringDataType dataType, String value) {
        this.requestId = requestId;
        this.timestamp = timestamp;
        this.dataType = dataType;
        this.value = value;
    }

    public String getRequestId() {
        return requestId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public MonitoringDataType getDataType() {
        return dataType;
    }

    public String getValue() {
        return value;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setDataType(MonitoringDataType dataType) {
        this.dataType = dataType;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "MonitoringRecord{requestId='" + requestId + "', timestamp=" + timestamp + ", dataType=" + dataType
                + ", value='" + value + "'}";
    }
}

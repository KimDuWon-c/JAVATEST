package FileIO;

import java.time.LocalDateTime;

public class FileRecord {
    private String requestId;
    private LocalDateTime timestamp;
    private DataType dataType;
    private String value;

    public FileRecord() {
    }

    public FileRecord(String requestId, LocalDateTime timestamp, DataType dataType, String value) {
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

    public DataType getDataType() {
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

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "FileRecord{requestId='" + requestId + "', timestamp=" + timestamp + ", dataType=" + dataType
                + ", value='" + value + "'}";
    }
}

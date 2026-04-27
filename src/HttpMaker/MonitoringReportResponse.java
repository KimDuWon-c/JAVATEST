package HttpMaker;

import java.util.List;

import FileIO.FileRecord;
import FileIO.MatchSummary;

public class MonitoringReportResponse {
    private boolean success;
    private String message;
    private AIAgentInfo agentInfo;
    private TimeWindow timeWindow;
    private MatchSummary matchSummary;
    private int monitoringRecordCount;
    private List<FileRecord> monitoringRecords;

    public MonitoringReportResponse() {
    }

    public MonitoringReportResponse(
            boolean success,
            String message,
            AIAgentInfo agentInfo,
            TimeWindow timeWindow,
            MatchSummary matchSummary,
            int monitoringRecordCount,
            List<FileRecord> monitoringRecords) {
        this.success = success;
        this.message = message;
        this.agentInfo = agentInfo;
        this.timeWindow = timeWindow;
        this.matchSummary = matchSummary;
        this.monitoringRecordCount = monitoringRecordCount;
        this.monitoringRecords = monitoringRecords;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public AIAgentInfo getAgentInfo() {
        return agentInfo;
    }

    public TimeWindow getTimeWindow() {
        return timeWindow;
    }

    public MatchSummary getMatchSummary() {
        return matchSummary;
    }

    public int getMonitoringRecordCount() {
        return monitoringRecordCount;
    }

    public List<FileRecord> getMonitoringRecords() {
        return monitoringRecords;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setAgentInfo(AIAgentInfo agentInfo) {
        this.agentInfo = agentInfo;
    }

    public void setTimeWindow(TimeWindow timeWindow) {
        this.timeWindow = timeWindow;
    }

    public void setMatchSummary(MatchSummary matchSummary) {
        this.matchSummary = matchSummary;
    }

    public void setMonitoringRecordCount(int monitoringRecordCount) {
        this.monitoringRecordCount = monitoringRecordCount;
    }

    public void setMonitoringRecords(List<FileRecord> monitoringRecords) {
        this.monitoringRecords = monitoringRecords;
    }
}

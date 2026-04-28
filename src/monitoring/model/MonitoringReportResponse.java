package monitoring.model;

import java.util.List;

public class MonitoringReportResponse {
    private boolean success;
    private String message;
    private AgentInfo agentInfo;
    private TimeWindow timeWindow;
    private MatchSummary matchSummary;
    private int monitoringRecordCount;
    private List<MonitoringRecord> monitoringRecords;

    public MonitoringReportResponse() {
    }

    public MonitoringReportResponse(
            boolean success,
            String message,
            AgentInfo agentInfo,
            TimeWindow timeWindow,
            MatchSummary matchSummary,
            int monitoringRecordCount,
            List<MonitoringRecord> monitoringRecords) {
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

    public AgentInfo getAgentInfo() {
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

    public List<MonitoringRecord> getMonitoringRecords() {
        return monitoringRecords;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setAgentInfo(AgentInfo agentInfo) {
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

    public void setMonitoringRecords(List<MonitoringRecord> monitoringRecords) {
        this.monitoringRecords = monitoringRecords;
    }
}

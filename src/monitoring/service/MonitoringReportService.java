package monitoring.service;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import monitoring.file.MonitoringFileService;
import monitoring.model.AgentInfo;
import monitoring.model.MatchSummary;
import monitoring.model.MonitoringDataType;
import monitoring.model.MonitoringRecord;
import monitoring.model.MonitoringReportRequest;
import monitoring.model.MonitoringReportResponse;
import monitoring.model.TimeRange;

// Main business service that converts a report request into a filtered monitoring response.
public class MonitoringReportService {
    private final AgentRegistry agentRegistry;
    private final MonitoringFileService fileService;

    public MonitoringReportService(AgentRegistry agentRegistry, MonitoringFileService fileService) {
        this.agentRegistry = agentRegistry;
        this.fileService = fileService;
    }

    // Validates the request, reads source data, applies time filtering, and builds the response DTO.
    public MonitoringReportResponse createReport(MonitoringReportRequest request) throws IOException {
        validateRequest(request);

        AgentInfo agentInfo = agentRegistry.findByModelName(request.getModelName());
        if (agentInfo == null) {
            throw new IllegalArgumentException("Unknown modelName: " + request.getModelName());
        }

        List<MonitoringRecord> allRecords = fileService.readMonitoringRecords(Paths.get(agentInfo.getMonitoringDataPath()));
        TimeRange range = fileService.createRangeFromHours(
                request.getTimeWindow().getStartHour(),
                request.getTimeWindow().getEndHour());

        List<MonitoringRecord> recordsInRange = fileService.filterByRange(allRecords, range);
        List<MonitoringRecord> monitoringRecords = fileService.filterByTypeAndRange(recordsInRange, MonitoringDataType.P, range);
        MatchSummary matchSummary = fileService.calculateMatchSummary(recordsInRange);

        return new MonitoringReportResponse(
                true,
                "Monitoring report created successfully.",
                agentInfo,
                request.getTimeWindow(),
                matchSummary,
                monitoringRecords.size(),
                monitoringRecords);
    }

    // Keeps request validation in one place so API and tests share the same rule set.
    private void validateRequest(MonitoringReportRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request body is required.");
        }
        if (isBlank(request.getModelName())) {
            throw new IllegalArgumentException("modelName is required.");
        }
        if (request.getTimeWindow() == null) {
            throw new IllegalArgumentException("timeWindow is required.");
        }
        if (isBlank(request.getTimeWindow().getStartHour())) {
            throw new IllegalArgumentException("timeWindow.startHour is required.");
        }
        if (isBlank(request.getTimeWindow().getEndHour())) {
            throw new IllegalArgumentException("timeWindow.endHour is required.");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}

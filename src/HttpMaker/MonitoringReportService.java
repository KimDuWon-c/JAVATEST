package HttpMaker;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import FileIO.DataType;
import FileIO.DateTimeRange;
import FileIO.FileRecord;
import FileIO.MatchSummary;
import FileIO.fileInput;

public class MonitoringReportService {
    private final AIAgentRegistry agentRegistry;
    private final fileInput fileService;

    public MonitoringReportService(AIAgentRegistry agentRegistry, fileInput fileService) {
        this.agentRegistry = agentRegistry;
        this.fileService = fileService;
    }

    public MonitoringReportResponse createReport(MonitoringReportRequest request) throws IOException {
        validateRequest(request);

        AIAgentInfo agentInfo = agentRegistry.findByModelName(request.getModelName());
        if (agentInfo == null) {
            throw new IllegalArgumentException("Unknown modelName: " + request.getModelName());
        }

        List<FileRecord> allRecords = fileService.readPredictionRecords(Paths.get(agentInfo.getMonitoringDataPath()));
        DateTimeRange range = fileService.createRangeFromHours(
                request.getTimeWindow().getStartHour(),
                request.getTimeWindow().getEndHour());

        List<FileRecord> recordsInRange = fileService.filterByRange(allRecords, range);
        List<FileRecord> monitoringRecords = fileService.filterByTypeAndRange(recordsInRange, DataType.P, range);
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

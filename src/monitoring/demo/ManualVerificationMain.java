package monitoring.demo;

import java.nio.file.Paths;
import java.util.List;

import org.eclipse.jetty.client.api.ContentResponse;

import monitoring.file.MonitoringFileService;
import monitoring.http.MonitoringHttpClient;
import monitoring.http.MonitoringHttpServer;
import monitoring.model.MatchSummary;
import monitoring.model.MonitoringDataType;
import monitoring.model.MonitoringRecord;
import monitoring.model.MonitoringReportRequest;
import monitoring.model.MonitoringReportResponse;
import monitoring.model.TimeRange;
import monitoring.model.TimeWindow;

// Manual smoke-test entry point for quick verification after copy/paste or code changes.
public class ManualVerificationMain {
    public static void main(String[] args) {
        MonitoringFileService fileService = new MonitoringFileService();

        try {
            runFileChecks(fileService);
            runHttpChecks();
            System.out.println("[TEST] reusable package checks passed");
        } catch (Exception exception) {
            System.err.println("[TEST] reusable package failed: " + exception.getMessage());
            exception.printStackTrace();
        }
    }

    private static void runFileChecks(MonitoringFileService fileService) throws Exception {
        List<MonitoringRecord> records = fileService.readMonitoringRecords(Paths.get("monitoring-data.txt"));
        MatchSummary summary = fileService.calculateMatchSummary(records);
        TimeRange range = fileService.createRangeFromHours("2025040100", "2025041010");
        List<MonitoringRecord> monitoringRecords = fileService.filterByTypeAndRange(records, MonitoringDataType.P, range);

        assertEquals(8, records.size(), "reusable record count");
        assertEquals(4, summary.getTotalComparableCount(), "reusable comparable count");
        assertEquals(2, summary.getMatchedCount(), "reusable matched count");
        assertEquals(2, summary.getUnmatchedCount(), "reusable unmatched count");
        assertEquals(3, monitoringRecords.size(), "reusable monitoring count in range");
    }

    private static void runHttpChecks() throws Exception {
        try (MonitoringHttpServer server = new MonitoringHttpServer(8080);
             MonitoringHttpClient client = new MonitoringHttpClient()) {
            server.start();
            client.start();

            MonitoringReportRequest request = new MonitoringReportRequest();
            request.setModelName("demo-model");

            TimeWindow timeWindow = new TimeWindow();
            timeWindow.setStartHour("2025040100");
            timeWindow.setEndHour("2025041010");
            request.setTimeWindow(timeWindow);

            ContentResponse response = client.postJson("http://127.0.0.1:8080/monitoring/report", request);
            MonitoringReportResponse report = client.fromJson(response.getContentAsString(), MonitoringReportResponse.class);

            assertEquals(200, response.getStatus(), "reusable HTTP status");
            assertTrue(report.isSuccess(), "reusable report success");
            assertEquals("demo-model", report.getAgentInfo().getModelName(), "reusable modelName");
            assertEquals(3, report.getMonitoringRecordCount(), "reusable monitoring record count");
            assertEquals(2, report.getMatchSummary().getMatchedCount(), "reusable matched count");
        }
    }

    private static void assertEquals(Object expected, Object actual, String label) {
        if (expected == null ? actual != null : !expected.equals(actual)) {
            throw new IllegalStateException(label + " expected=" + expected + ", actual=" + actual);
        }
        System.out.println("[PASS] " + label + " -> " + actual);
    }

    private static void assertTrue(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException(label + " expected=true, actual=false");
        }
        System.out.println("[PASS] " + label);
    }
}

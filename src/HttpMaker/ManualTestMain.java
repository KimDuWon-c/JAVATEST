package HttpMaker;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.eclipse.jetty.client.api.ContentResponse;

import FileIO.DataType;
import FileIO.DateTimeRange;
import FileIO.FileRecord;
import FileIO.MatchSummary;
import FileIO.fileInput;

public class ManualTestMain {
    public static void main(String[] args) {
        fileInput fileService = new fileInput();

        try {
            runFileParsingChecks(fileService);
            runHttpIntegrationCheck();
            System.out.println("[TEST] all checks passed");
        } catch (Exception exception) {
            System.err.println("[TEST] failed: " + exception.getMessage());
            exception.printStackTrace();
        }
    }

    private static void runFileParsingChecks(fileInput fileService) throws Exception {
        List<FileRecord> records = fileService.readPredictionRecords(Paths.get("monitoring-data.txt"));
        MatchSummary summary = fileService.calculateMatchSummary(records);
        DateTimeRange range = fileService.createRangeFromHours("2025040100", "2025041010");
        List<FileRecord> monitoringRecords = fileService.filterByTypeAndRange(records, DataType.P, range);

        assertEquals(8, records.size(), "record count");
        assertEquals(4, summary.getTotalComparableCount(), "comparable count in full sample");
        assertEquals(2, summary.getMatchedCount(), "matched count in full sample");
        assertEquals(2, summary.getUnmatchedCount(), "unmatched count in full sample");
        assertEquals(3, monitoringRecords.size(), "monitoring record count in range");
    }

    private static void runHttpIntegrationCheck() throws Exception {
        try (HttpReceiver httpReceiver = new HttpReceiver(8080); HttpSender httpSender = new HttpSender()) {
            httpReceiver.start();
            httpSender.start();

            MonitoringReportRequest request = new MonitoringReportRequest();
            request.setModelName("demo-model");

            TimeWindow timeWindow = new TimeWindow();
            timeWindow.setStartHour("2025040100");
            timeWindow.setEndHour("2025041010");
            request.setTimeWindow(timeWindow);

            ContentResponse response = httpSender.postJson("http://127.0.0.1:8080/monitoring/report", request);
            MonitoringReportResponse report = httpSender.fromJson(response.getContentAsString(), MonitoringReportResponse.class);

            assertEquals(200, response.getStatus(), "HTTP status");
            assertTrue(report.isSuccess(), "report success");
            assertEquals("demo-model", report.getAgentInfo().getModelName(), "modelName");
            assertEquals(3, report.getMonitoringRecordCount(), "monitoring record count");
            assertEquals(2, report.getMatchSummary().getMatchedCount(), "matched count");
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

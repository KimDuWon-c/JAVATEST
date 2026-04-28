package monitoring.demo;

import java.nio.file.Paths;
import java.util.List;

import org.eclipse.jetty.client.api.ContentResponse;

import monitoring.file.MonitoringFileService;
import monitoring.http.MonitoringHttpClient;
import monitoring.http.MonitoringHttpServer;
import monitoring.model.MatchSummary;
import monitoring.model.MonitoringRecord;
import monitoring.model.MonitoringReportRequest;
import monitoring.model.MonitoringReportResponse;
import monitoring.model.TimeRange;
import monitoring.model.TimeWindow;

// Small runnable example that shows file parsing and HTTP report flow together.
public class DemoMain {
    public static void main(String[] args) {
        MonitoringFileService fileService = new MonitoringFileService();

        try (MonitoringHttpServer server = new MonitoringHttpServer(8080);
             MonitoringHttpClient client = new MonitoringHttpClient()) {

            List<MonitoringRecord> records = fileService.readMonitoringRecords(Paths.get("monitoring-data.txt"));
            MatchSummary matchSummary = fileService.calculateMatchSummary(records);
            TimeRange range = fileService.createMonthlyRangeFromHour("2025041010");

            System.out.println("[DEMO] parsed records");
            records.forEach(System.out::println);

            System.out.println("[DEMO] match summary");
            System.out.println(matchSummary);

            System.out.println("[DEMO] monthly range");
            System.out.println(range);

            server.start();
            client.start();

            MonitoringReportRequest request = new MonitoringReportRequest();
            request.setModelName("demo-model");

            TimeWindow timeWindow = new TimeWindow();
            timeWindow.setStartHour("2025040100");
            timeWindow.setEndHour("2025041010");
            request.setTimeWindow(timeWindow);

            ContentResponse response = client.postJson("http://127.0.0.1:8080/monitoring/report", request);
            MonitoringReportResponse report =
                    client.fromJson(response.getContentAsString(), MonitoringReportResponse.class);

            System.out.println("[DEMO] report response raw");
            System.out.println(response.getContentAsString());
            System.out.println("[DEMO] report matched count = " + report.getMatchSummary().getMatchedCount());
        } catch (Exception exception) {
            System.err.println("[DEMO] failed: " + exception.getMessage());
            exception.printStackTrace();
        }
    }
}

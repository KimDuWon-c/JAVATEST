package HttpMaker;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.eclipse.jetty.client.api.ContentResponse;

import FileIO.DateTimeRange;
import FileIO.FileRecord;
import FileIO.MatchSummary;
import FileIO.Person;
import FileIO.fileInput;

public class MainHttp {
    public static void main(String[] args) {
        fileInput fileService = new fileInput();
        Path textFile = Paths.get("monitoring-data.txt");
        Path jsonFile = Paths.get("person.json");

        try (HttpReceiver httpReceiver = new HttpReceiver(8080); HttpSender httpSender = new HttpSender()) {
            List<String> rawLines = fileService.readLines(textFile);
            List<FileRecord> records = fileService.readPredictionRecords(textFile);
            MatchSummary matchSummary = fileService.calculateMatchSummary(records);
            DateTimeRange monthlyRange = fileService.createMonthlyRangeFromHour("2025041010");
            List<FileRecord> monitoringInRange = fileService.findMonitoringRecordsForMonthlyRange(records, "2025041010");
            Person person = fileService.readJson(jsonFile, Person.class);

            System.out.println("[FILE] raw lines");
            rawLines.forEach(System.out::println);

            System.out.println("[FILE] parsed records");
            records.forEach(System.out::println);

            System.out.println("[FILE] match summary");
            System.out.println(matchSummary);

            System.out.println("[FILE] monthly monitoring range");
            System.out.println(monthlyRange);

            System.out.println("[FILE] monitoring records in range");
            monitoringInRange.forEach(System.out::println);

            System.out.println("[JSON] person");
            System.out.println(person);

            httpReceiver.start();
            httpSender.start();

            MonitoringReportRequest reportRequest = new MonitoringReportRequest();
            reportRequest.setModelName("demo-model");

            TimeWindow timeWindow = new TimeWindow();
            timeWindow.setStartHour("2025040100");
            timeWindow.setEndHour("2025041010");
            reportRequest.setTimeWindow(timeWindow);

            ContentResponse postResponse = httpSender.postJson(
                    "http://127.0.0.1:8080/monitoring/report",
                    reportRequest);
            MonitoringReportResponse reportResponse = httpSender.fromJson(
                    postResponse.getContentAsString(),
                    MonitoringReportResponse.class);

            System.out.println("[HTTP] report response raw");
            System.out.println(postResponse.getContentAsString());
            System.out.println("[HTTP] report response object");
            System.out.println(fileService.toJson(reportResponse));
            System.out.println("[HTTP] matched count = " + reportResponse.getMatchSummary().getMatchedCount());
        } catch (Exception exception) {
            System.err.println("Application error: " + exception.getMessage());
            exception.printStackTrace();
        }
    }
}

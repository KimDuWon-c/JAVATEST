# Jetty 9 + Gson File IO and REST Example

This project is built around:

- Embedded HTTP server: Jetty 9
- HTTP client: Jetty 9 HttpClient
- JSON parser: Gson 2.8.6

## Eclipse setup

This project is intended to work in Eclipse.

Recommended setup:

- Import as a normal Java Project
- Use JDK 8 or newer for the main `FileIO` and `HttpMaker` code
- Add every jar in `libs` to the project's Build Path
- Keep the working directory as the project root

If the working directory is the project root, files like `monitoring-data.txt` and `person.json` will be found correctly.

## Eclipse step-by-step setup

### 1. Import project

In Eclipse:

1. `File`
2. `Open Projects from File System...`
3. Select this folder:

```text
C:\Users\User\OneDrive\바탕 화면\공부\java
```

If that does not work well, use:

1. `File`
2. `New`
3. `Java Project`
4. Project name: `java`
5. Uncheck module-related options if Eclipse asks

### 2. Set Java 8 JRE

In Eclipse:

1. Right click project
2. `Build Path`
3. `Configure Build Path`
4. Open `Libraries`
5. Remove wrong JRE if needed
6. `Add Library...`
7. `JRE System Library`
8. Choose `Installed JREs...`
9. Add or select Java 8

If Java 8 is not registered in Eclipse yet:

1. `Window`
2. `Preferences`
3. `Java`
4. `Installed JREs`
5. `Add...`
6. Choose `Standard VM`
7. JRE home:

```text
C:\Users\User\OneDrive\바탕 화면\공부\java\.local-jdk8\jdk8u482-b08
```

If the exact folder name is slightly different, choose the real extracted JDK folder under `.local-jdk8`.

After that:

1. Select the Java 8 JRE
2. Apply
3. Set project compiler level to 1.8 if Eclipse asks

### 3. Add jar libraries

In Eclipse:

1. Right click project
2. `Build Path`
3. `Configure Build Path`
4. `Libraries`
5. `Add External JARs...`
6. Add all jars inside:

```text
C:\Users\User\OneDrive\바탕 화면\공부\java\libs
```

Expected jars:

- `gson-2.8.6.jar`
- `jetty-client-9.4.51.v20230217.jar`
- `jetty-http-9.4.51.v20230217.jar`
- `jetty-io-9.4.51.v20230217.jar`
- `jetty-security-9.4.51.v20230217.jar`
- `jetty-server-9.4.51.v20230217.jar`
- `jetty-servlet-9.4.51.v20230217.jar`
- `jetty-util-9.4.51.v20230217.jar`
- `jetty-util-ajax-9.4.51.v20230217.jar`
- `servlet-api-3.1.jar`

### 4. Set source folders

This project mainly uses:

- `src/FileIO`
- `src/HttpMaker`

If Eclipse asks about source folders, keep `src` as the source root.

### 5. Confirm working directory

This is important because the code reads:

- `monitoring-data.txt`
- `person.json`

Working directory should be the project root:

```text
C:\Users\User\OneDrive\바탕 화면\공부\java
```

## Eclipse Run Configuration

### Run the demo flow

Main class:

```text
HttpMaker.MainHttp
```

How:

1. Right click [`MainHttp.java`](</C:\Users\User\OneDrive\바탕 화면\공부\java\src\HttpMaker\MainHttp.java>)
2. `Run As`
3. `Java Application`

Recommended Run Configuration check:

1. `Run`
2. `Run Configurations...`
3. Select `HttpMaker.MainHttp`
4. `Arguments` tab
5. `Working directory`
6. Choose `Other`
7. Set:

```text
C:\Users\User\OneDrive\바탕 화면\공부\java
```

What it does:

- reads `monitoring-data.txt`
- parses monitoring records
- starts Jetty server on `8080`
- sends JSON request from client
- prints report JSON

### Run manual verification

Main class:

```text
HttpMaker.ManualTestMain
```

How:

1. Right click [`ManualTestMain.java`](</C:\Users\User\OneDrive\바탕 화면\공부\java\src\HttpMaker\ManualTestMain.java>)
2. `Run As`
3. `Java Application`

Recommended Run Configuration:

1. `Run`
2. `Run Configurations...`
3. Select `HttpMaker.ManualTestMain`
4. Set working directory to:

```text
C:\Users\User\OneDrive\바탕 화면\공부\java
```

Expected success output includes:

- `[PASS] record count`
- `[PASS] HTTP status -> 200`
- `[TEST] all checks passed`

## Eclipse checklist

If Eclipse does not run correctly, check these in order:

1. Project JRE is Java 8 or newer
2. Compiler compliance level is 1.8 or higher
3. All jars in `libs` are added
4. Working directory is project root
5. `monitoring-data.txt` exists in project root
6. `person.json` exists in project root
7. Port `8080` is not already used by another program

## Optional .classpath guidance

If you want to compare with Eclipse project settings, the important classpath idea is:

- one source folder: `src`
- one output folder: `bin`
- one JRE container: JavaSE-1.8
- all jar files in `libs`

Conceptually it should look like this:

```text
src/
bin/
JRE System Library [JavaSE-1.8]
libs/gson-2.8.6.jar
libs/jetty-client-9.4.51.v20230217.jar
libs/jetty-http-9.4.51.v20230217.jar
libs/jetty-io-9.4.51.v20230217.jar
libs/jetty-security-9.4.51.v20230217.jar
libs/jetty-server-9.4.51.v20230217.jar
libs/jetty-servlet-9.4.51.v20230217.jar
libs/jetty-util-9.4.51.v20230217.jar
libs/jetty-util-ajax-9.4.51.v20230217.jar
libs/servlet-api-3.1.jar
```

## Java version note

The main project code is now written to be compatible with JDK 8+.

That means:

- `src/FileIO`
- `src/HttpMaker`

are intended to run on Java 8 or newer.

Why this was changed:

- `JDK 11+` features such as `Path.of`, `Map.of`, and `String.isBlank()` were removed from the main flow
- This makes Eclipse setup easier on older environments

About `JDK 11+`:

- JDK 11 was officially released in September 2018
- So when someone says `JDK 11+`, they usually mean Java environments from September 2018 onward

Important note:

- The older experimental `src/purejava` folder still contains newer Java APIs
- The main service flow of this project is `src/FileIO` and `src/HttpMaker`
- If your goal is this monitoring/report project, use those packages

## Library compatibility note

Current libraries in this project:

- Jetty `9.4.51.v20230217`
- Gson `2.8.6`

Compatibility summary for this project:

- JDK 8
  - Recommended minimum version for the current main code
  - Jetty 9.4 branch officially targets Java 8+
  - Gson 2.8.6 is compatible

- JDK 11
  - Expected to work for this project
  - Your code no longer depends on Java 11-only APIs in the main flow
  - Jetty 9.4 is older, but Java 11 usage is generally reasonable

- JDK 17
  - Often usable in practice for this kind of Jetty 9.4 + Gson project
  - But this combination should be treated as "verify in your environment"
  - If you use newer JDKs in production, manual integration testing is strongly recommended

- JDK 21+
  - Possible, but less safe to assume without environment testing
  - Jetty 9.4 is now old enough that library upgrade planning is a good idea

Official-source-based notes:

- Jetty official branch table lists `jetty-9.4.x` as `Java 8+`
- Jetty official docs also show `jetty-9.4.x` is now `End of Community Support`
- Gson official README says `Gson 2.8.9 and older: Java 6`

Practical conclusion:

- Your project code: JDK 8+
- Gson 2.8.6: safe for JDK 8 / 11 / 17
- Jetty 9.4: acceptable for JDK 8 / 11 and likely workable on JDK 17, but because the branch is old and community support has ended, test before relying on higher JDKs

If you want the safest long-term setup:

- Keep this code style as JDK 8 compatible
- But consider upgrading Jetty later if your runtime standard becomes JDK 17+

## Run targets

Use these classes in Eclipse:

- `HttpMaker.MainHttp`
  - Full demo run
  - Reads file data
  - Starts Jetty server
  - Sends JSON request from client
  - Prints JSON response

- `HttpMaker.ManualTestMain`
  - Manual verification run
  - Prints `[PASS]` for each check
  - Good for quick regression checks after code changes

## Input file format

Monitoring data file example:

```text
req001#2025040100#P#0
req001#2025040100#A#0
req002#2025040101#P#1
req002#2025040101#A#0
req003#2025041010#P#1
req003#2025041010#A#1
```

Meaning:

- column 1: `requestId`
- column 2: `timestamp` in `yyyyMMddHH`
- column 3: `dataType`
  - `P` = monitoring value
  - `A` = prediction value
- column 4: `value`

## HTTP API

Endpoint:

```text
POST /monitoring/report
```

Request JSON:

```json
{
  "modelName": "demo-model",
  "timeWindow": {
    "startHour": "2025040100",
    "endHour": "2025041010"
  }
}
```

Response JSON includes:

- `success`
- `message`
- `agentInfo`
- `timeWindow`
- `matchSummary`
- `monitoringRecordCount`
- `monitoringRecords`

## Class overview

`src/FileIO/fileInput.java`

- Generic file service
- Reads text files
- Parses lines with custom parser logic
- Reads and writes JSON using Gson
- Calculates monitoring match summary
- Builds time ranges and filters records

`src/FileIO/FileRecord.java`

- One parsed monitoring line
- Holds `requestId`, `timestamp`, `dataType`, `value`

`src/FileIO/MatchSummary.java`

- Summary of prediction accuracy
- Holds comparable count, matched count, unmatched count

`src/HttpMaker/HttpReceiver.java`

- Jetty embedded server wrapper
- Exposes `/monitoring/report`
- Reads request JSON
- Calls report service
- Returns response JSON

`src/HttpMaker/HttpSender.java`

- Jetty HttpClient wrapper
- Sends JSON request
- Converts object to JSON
- Converts JSON string to Java object

`src/HttpMaker/MonitoringReportService.java`

- Main business service for report generation
- Finds AI agent by `modelName`
- Reads monitoring file
- Applies time filtering
- Builds report response

`src/HttpMaker/AIAgentRegistry.java`

- In-memory model registry
- Maps `modelName` to agent metadata and data file path

## Method reference

### `FileIO.fileInput`

#### `readLines(Path path)`

Purpose:

- Reads the file as raw text lines

When to use:

- When you just want the original file lines
- When parsing logic will be handled later

Example:

```java
fileInput fileService = new fileInput();
List<String> lines = fileService.readLines(java.nio.file.Paths.get("monitoring-data.txt"));
```

Returns:

- `List<String>`

#### `readParsedLines(Path path, LineParser<T> parser)`

Purpose:

- Reads a file line by line and parses each line with custom logic

When to use:

- When each file has different parse rules
- When you want reusable file reading and separate parser logic

Example:

```java
fileInput fileService = new fileInput();
List<FileRecord> records = fileService.readParsedLines(
    java.nio.file.Paths.get("monitoring-data.txt"),
    fileService::parsePredictionRecord
);
```

Returns:

- `List<T>`

#### `parseLines(List<String> lines, LineParser<T> parser)`

Purpose:

- Parses already loaded lines with custom logic

When to use:

- When lines are already in memory
- When the same raw lines must be parsed in multiple ways

Example:

```java
List<String> lines = fileService.readLines(java.nio.file.Paths.get("monitoring-data.txt"));
List<FileRecord> records = fileService.parseLines(lines, fileService::parsePredictionRecord);
```

Returns:

- `List<T>`

#### `readKeyValueFile(Path path)`

Purpose:

- Reads a file where each line is `key value`

When to use:

- For simple config-like text files

Example:

```java
Map<String, String> values = fileService.readKeyValueFile(java.nio.file.Paths.get("input.txt"));
```

Returns:

- `Map<String, String>`

#### `readPredictionRecords(Path path)`

Purpose:

- Reads monitoring-format records like `req001#2025040100#P#0`

When to use:

- When the file already uses the monitoring/prediction format of this project

Example:

```java
List<FileRecord> records = fileService.readPredictionRecords(java.nio.file.Paths.get("monitoring-data.txt"));
```

Returns:

- `List<FileRecord>`

#### `parsePredictionRecord(String line, int lineNumber)`

Purpose:

- Parses one monitoring-format line into `FileRecord`

When to use:

- When using custom line parsing
- When you need line-number-aware error messages

Example:

```java
FileRecord record = fileService.parsePredictionRecord("req001#2025040100#P#0", 1);
```

Returns:

- `FileRecord`

#### `parseKeyValueLine(String line, int lineNumber)`

Purpose:

- Parses one `key value` line

When to use:

- For simple custom parsing based on whitespace

Example:

```java
Map.Entry<String, String> entry = fileService.parseKeyValueLine("host localhost", 1);
```

Returns:

- `Map.Entry<String, String>`

#### `calculateMatchSummary(List<FileRecord> records)`

Purpose:

- Compares latest `P` and latest `A` for each `requestId`
- Counts matched and unmatched results

When to use:

- When you want a simple performance summary

Matching rule:

- same `requestId`
- latest `P` value equals latest `A` value

Example:

```java
MatchSummary summary = fileService.calculateMatchSummary(records);
System.out.println(summary.getMatchedCount());
```

Returns:

- `MatchSummary`

#### `filterByTypeAndRange(List<FileRecord> records, DataType dataType, DateTimeRange range)`

Purpose:

- Filters records by both type and time range

When to use:

- When you want only `P` records or only `A` records in a time window

Example:

```java
DateTimeRange range = fileService.createRangeFromHours("2025040100", "2025041010");
List<FileRecord> monitoring = fileService.filterByTypeAndRange(records, DataType.P, range);
```

Returns:

- `List<FileRecord>`

#### `filterByRange(List<FileRecord> records, DateTimeRange range)`

Purpose:

- Filters records only by time range

When to use:

- When both `P` and `A` records are needed inside the same window

Example:

```java
DateTimeRange range = fileService.createRangeFromHours("2025040100", "2025041010");
List<FileRecord> recordsInRange = fileService.filterByRange(records, range);
```

Returns:

- `List<FileRecord>`

#### `createMonthlyRangeFromHour(String yyyyMMddHH)`

Purpose:

- Creates a range from the first day of the month to the requested hour's end minute/second

Example:

- Input: `2025041010`
- Range: `2025-04-01 00:00:00` to `2025-04-10 10:59:59`

When to use:

- When monthly accumulation is needed up to a target hour

Example:

```java
DateTimeRange range = fileService.createMonthlyRangeFromHour("2025041010");
```

Returns:

- `DateTimeRange`

#### `findMonitoringRecordsForMonthlyRange(List<FileRecord> records, String yyyyMMddHH)`

Purpose:

- Gets only `P` records from the month start to the target hour

When to use:

- When you want monitoring-only monthly lookup in one call

Example:

```java
List<FileRecord> monitoring = fileService.findMonitoringRecordsForMonthlyRange(records, "2025041010");
```

Returns:

- `List<FileRecord>`

#### `createRangeFromHours(String startHour, String endHour)`

Purpose:

- Builds a direct time range from `startHour` to `endHour`

Rule:

- `startHour` becomes `HH:00:00`
- `endHour` becomes `HH:59:59`

When to use:

- When the client sends explicit start and end hours

Example:

```java
DateTimeRange range = fileService.createRangeFromHours("2025040100", "2025041010");
```

Returns:

- `DateTimeRange`

#### `groupByRequestId(List<FileRecord> records)`

Purpose:

- Groups records by request id

When to use:

- When you want to inspect records request-by-request

Example:

```java
Map<String, List<FileRecord>> grouped = fileService.groupByRequestId(records);
```

Returns:

- `Map<String, List<FileRecord>>`

#### `writeLines(Path path, List<String> lines)`

Purpose:

- Writes text lines to a file

When to use:

- When you want to store generated text output

Example:

```java
java.util.List<String> outputLines = new java.util.ArrayList<String>();
outputLines.add("line1");
outputLines.add("line2");
fileService.writeLines(java.nio.file.Paths.get("output.txt"), outputLines);
```

#### `readJson(Path path, Class<T> type)`

Purpose:

- Reads JSON file into Java object using Gson

When to use:

- When loading config, sample DTO, or model file

Example:

```java
Person person = fileService.readJson(java.nio.file.Paths.get("person.json"), Person.class);
```

Returns:

- `T`

#### `writeJson(Path path, Object value)`

Purpose:

- Writes Java object as JSON file using Gson

When to use:

- When saving DTO or result data as JSON

Example:

```java
fileService.writeJson(java.nio.file.Paths.get("person-copy.json"), person);
```

#### `toJson(Object value)`

Purpose:

- Converts Java object to JSON string

When to use:

- When sending HTTP JSON body
- When printing JSON log

Example:

```java
String json = fileService.toJson(person);
```

Returns:

- `String`

### `HttpMaker.HttpSender`

#### `start()`

Purpose:

- Starts Jetty `HttpClient`

When to use:

- Before sending requests

Example:

```java
HttpSender sender = new HttpSender();
sender.start();
```

#### `get(String targetUrl, Map<String, String> headers)`

Purpose:

- Sends HTTP GET request

When to use:

- For simple test or read-only endpoint calls

Example:

```java
ContentResponse response = sender.get(
    "http://127.0.0.1:8080/health",
    java.util.Collections.singletonMap("x-requestId", "health-check")
);
```

Returns:

- `ContentResponse`

#### `postJson(String targetUrl, String jsonBody)`

Purpose:

- Sends HTTP POST request with raw JSON string

When to use:

- When JSON text is already prepared

Example:

```java
String body = "{\"modelName\":\"demo-model\"}";
ContentResponse response = sender.postJson("http://127.0.0.1:8080/monitoring/report", body);
```

Returns:

- `ContentResponse`

#### `postJson(String targetUrl, Object body)`

Purpose:

- Converts Java object to JSON and sends POST request

When to use:

- Best choice for DTO-based request sending

Example:

```java
MonitoringReportRequest request = new MonitoringReportRequest();
request.setModelName("demo-model");

TimeWindow timeWindow = new TimeWindow();
timeWindow.setStartHour("2025040100");
timeWindow.setEndHour("2025041010");
request.setTimeWindow(timeWindow);

ContentResponse response = sender.postJson("http://127.0.0.1:8080/monitoring/report", request);
```

Returns:

- `ContentResponse`

#### `fromJson(String json, Class<T> type)`

Purpose:

- Converts JSON string to Java object

When to use:

- After receiving server response JSON

Example:

```java
MonitoringReportResponse report =
    sender.fromJson(response.getContentAsString(), MonitoringReportResponse.class);
```

Returns:

- `T`

#### `close()`

Purpose:

- Stops Jetty `HttpClient`

When to use:

- After request work is finished
- Usually via try-with-resources

Example:

```java
try (HttpSender sender = new HttpSender()) {
    sender.start();
}
```

### `HttpMaker.HttpReceiver`

#### `HttpReceiver(int port)`

Purpose:

- Creates server with default monitoring report service

When to use:

- For normal project run

Example:

```java
HttpReceiver receiver = new HttpReceiver(8080);
```

#### `HttpReceiver(int port, MonitoringReportService monitoringReportService)`

Purpose:

- Creates server with custom business service

When to use:

- For custom test setup
- For changing registry or file source

Example:

```java
MonitoringReportService service =
    new MonitoringReportService(new AIAgentRegistry(), new fileInput());
HttpReceiver receiver = new HttpReceiver(8080, service);
```

#### `start()`

Purpose:

- Starts Jetty embedded server

Example:

```java
receiver.start();
```

#### `stop()`

Purpose:

- Stops Jetty embedded server

Example:

```java
receiver.stop();
```

#### `close()`

Purpose:

- Stops server automatically in try-with-resources

Example:

```java
try (HttpReceiver receiver = new HttpReceiver(8080)) {
    receiver.start();
}
```

### `HttpMaker.MonitoringReportService`

#### `createReport(MonitoringReportRequest request)`

Purpose:

- Main business method for monitoring report generation

What it does:

1. Validates request
2. Finds AI agent by `modelName`
3. Reads monitoring data file
4. Builds requested time range
5. Filters records inside range
6. Extracts monitoring `P` records
7. Calculates match summary
8. Returns `MonitoringReportResponse`

Example:

```java
MonitoringReportService service =
    new MonitoringReportService(new AIAgentRegistry(), new fileInput());

MonitoringReportRequest request = new MonitoringReportRequest();
request.setModelName("demo-model");

TimeWindow timeWindow = new TimeWindow();
timeWindow.setStartHour("2025040100");
timeWindow.setEndHour("2025041010");
request.setTimeWindow(timeWindow);

MonitoringReportResponse response = service.createReport(request);
```

Returns:

- `MonitoringReportResponse`

### `HttpMaker.AIAgentRegistry`

#### `AIAgentRegistry()`

Purpose:

- Creates default model registry

Current default:

- `demo-model` -> `monitoring-data.txt`

Example:

```java
AIAgentRegistry registry = new AIAgentRegistry();
```

#### `register(AIAgentInfo agentInfo)`

Purpose:

- Adds a new model-to-agent mapping

When to use:

- When more AI agents are added later

Example:

```java
registry.register(new AIAgentInfo(
    "gpt-model-a",
    "agent-002",
    "Forecast Agent",
    "forecast-data.txt",
    "Forecast monitoring agent"
));
```

#### `findByModelName(String modelName)`

Purpose:

- Finds agent metadata by model name

Example:

```java
AIAgentInfo info = registry.findByModelName("demo-model");
```

Returns:

- `AIAgentInfo`

## End-to-end call example

```java
try (HttpReceiver receiver = new HttpReceiver(8080);
     HttpSender sender = new HttpSender()) {

    receiver.start();
    sender.start();

    MonitoringReportRequest request = new MonitoringReportRequest();
    request.setModelName("demo-model");

    TimeWindow timeWindow = new TimeWindow();
    timeWindow.setStartHour("2025040100");
    timeWindow.setEndHour("2025041010");
    request.setTimeWindow(timeWindow);

    ContentResponse response = sender.postJson(
        "http://127.0.0.1:8080/monitoring/report",
        request
    );

    MonitoringReportResponse report =
        sender.fromJson(response.getContentAsString(), MonitoringReportResponse.class);

    System.out.println(report.getMonitoringRecordCount());
}
```

## Compile example

Use the jars in `libs`.

```powershell
javac -cp "libs/*" -d bin src\FileIO\*.java src\HttpMaker\*.java
```

## Run example

```powershell
java -cp "bin;libs/*" HttpMaker.MainHttp
```

## Manual test example

```powershell
java -cp "bin;libs/*" HttpMaker.ManualTestMain
```


1. 파일 파싱
readLines(...)
readParsedLines(...)
parseLines(...)

2. 파일 파싱 및 고급화

FileRecord.java
DataType.java
DateTimeRange.java
MatchSummary.java

readPredictionRecords(...)
parsePredictionRecord(...)
calculateMatchSummary(...)
createMonthlyRangeFromHour(...)
createRangeFromHours(...)
filterByRange(...)
filterByTypeAndRange(...)
findMonitoringRecordsForMonthlyRange(...)
groupByRequestId(...)

P/A 비교
시간 범위 조회
requestId별 그룹화
같은 실제 업무용 해석까지 붙는 단계입니다.
3. HTTP 통신 시작
HttpReceiver.java
HttpSender.java
역할은 단순합니다.

HttpReceiver는 Jetty 서버를 띄움
HttpSender는 Jetty HttpClient로 요청을 보냄

4. 통신 고급화
MonitoringReportService.java
AIAgentRegistry.java
AIAgentInfo.java
MonitoringReportRequest.java
TimeWindow.java
MonitoringReportResponse.java
ErrorResponse.java
여기서는 흐름이 이렇게 됩니다.

클라이언트가 modelName, timeWindow를 JSON으로 보냄
서버가 요청 DTO로 받음
MonitoringReportService가 에이전트 정보 조회
연결된 파일을 읽음
시간 범위와 P/A 집계를 적용
응답 DTO를 JSON으로 반환
즉 이 단계는 “HTTP가 된다”를 넘어서 “업무 의미를 가진 API 서비스”가 됩니다.

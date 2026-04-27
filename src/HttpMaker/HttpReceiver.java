package HttpMaker;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

import com.google.gson.Gson;

public class HttpReceiver implements AutoCloseable {
    private final Gson gson;
    private final Server server;

    public HttpReceiver(int port) {
        this(port, new MonitoringReportService(new AIAgentRegistry(), new FileIO.fileInput()));
    }

    public HttpReceiver(int port, MonitoringReportService monitoringReportService) {
        this.gson = new Gson();
        this.server = new Server(port);
        this.server.setHandler(new MonitoringReportHandler(monitoringReportService));
    }

    public void start() throws Exception {
        server.start();
    }

    public void stop() throws Exception {
        server.stop();
    }

    @Override
    public void close() throws Exception {
        if (server.isStarted() || server.isStarting()) {
            server.stop();
        }
    }

    private class MonitoringReportHandler extends AbstractHandler {
        private final MonitoringReportService monitoringReportService;

        private MonitoringReportHandler(MonitoringReportService monitoringReportService) {
            this.monitoringReportService = monitoringReportService;
        }

        @Override
        public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
                throws IOException, ServletException {
            if (!"/monitoring/report".equals(target)) {
                return;
            }

            baseRequest.setHandled(true);
            response.setContentType("application/json;charset=utf-8");

            if (!"POST".equalsIgnoreCase(request.getMethod())) {
                response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                response.getWriter().write(gson.toJson(new ErrorResponse("Only POST is supported.")));
                return;
            }

            try {
                MonitoringReportRequest reportRequest = gson.fromJson(readBody(request), MonitoringReportRequest.class);
                MonitoringReportResponse reportResponse = monitoringReportService.createReport(reportRequest);
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(gson.toJson(reportResponse));
            } catch (IllegalArgumentException exception) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(gson.toJson(new ErrorResponse(exception.getMessage())));
            } catch (Exception exception) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write(gson.toJson(new ErrorResponse(exception.getMessage())));
            }
        }

        private String readBody(HttpServletRequest request) throws IOException {
            StringBuilder requestBody = new StringBuilder();

            try (BufferedReader reader = request.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    requestBody.append(line);
                }
            }

            return requestBody.toString();
        }
    }
}

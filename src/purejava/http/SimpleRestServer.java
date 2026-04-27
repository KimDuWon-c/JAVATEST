package purejava.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public class SimpleRestServer {
    private final HttpServer server;

    public SimpleRestServer(int port) throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/api/echo", new EchoHandler());
    }

    public void start() {
        server.start();
    }

    public void stop(int delaySeconds) {
        server.stop(delaySeconds);
    }

    private static class EchoHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            URI uri = exchange.getRequestURI();
            Map<String, String> queryParams = parseQuery(uri.getRawQuery());
            String requestBody = readRequestBody(exchange);

            String responseJson = "{"
                    + "\"method\":\"" + method + "\","
                    + "\"path\":\"" + escape(uri.getPath()) + "\","
                    + "\"query\":" + mapToJson(queryParams) + ","
                    + "\"body\":\"" + escape(requestBody) + "\""
                    + "}";

            byte[] responseBytes = responseJson.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
            exchange.sendResponseHeaders(200, responseBytes.length);

            try (OutputStream outputStream = exchange.getResponseBody()) {
                outputStream.write(responseBytes);
            }
        }

        private String readRequestBody(HttpExchange exchange) throws IOException {
            try (InputStream inputStream = exchange.getRequestBody()) {
                return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            }
        }

        private Map<String, String> parseQuery(String rawQuery) {
            Map<String, String> params = new LinkedHashMap<>();
            if (rawQuery == null || rawQuery.isBlank()) {
                return params;
            }

            for (String pair : rawQuery.split("&")) {
                String[] tokens = pair.split("=", 2);
                String key = tokens[0];
                String value = tokens.length > 1 ? tokens[1] : "";
                params.put(key, value);
            }

            return params;
        }

        private String mapToJson(Map<String, String> map) {
            StringBuilder builder = new StringBuilder("{");
            boolean first = true;

            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (!first) {
                    builder.append(",");
                }

                builder.append("\"")
                        .append(escape(entry.getKey()))
                        .append("\":\"")
                        .append(escape(entry.getValue()))
                        .append("\"");
                first = false;
            }

            builder.append("}");
            return builder.toString();
        }

        private String escape(String value) {
            return value.replace("\\", "\\\\").replace("\"", "\\\"");
        }
    }
}

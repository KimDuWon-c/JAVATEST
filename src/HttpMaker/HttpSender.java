package HttpMaker;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.Gson;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpMethod;

public class HttpSender implements AutoCloseable {
    private final Gson gson;
    private final HttpClient httpClient;

    public HttpSender() {
        this.gson = new Gson();
        this.httpClient = new HttpClient();
    }

    public void start() throws Exception {
        if (!httpClient.isStarted()) {
            httpClient.start();
        }
    }

    public ContentResponse get(String targetUrl, Map<String, String> headers) throws Exception {
        return send(HttpMethod.GET, targetUrl, headers, null);
    }

    public ContentResponse postJson(String targetUrl, String jsonBody) throws Exception {
        Map<String, String> headers = new LinkedHashMap<String, String>();
        headers.put("Content-Type", "application/json");
        return send(HttpMethod.POST, targetUrl, headers, jsonBody);
    }

    public ContentResponse postJson(String targetUrl, Object body) throws Exception {
        return postJson(targetUrl, gson.toJson(body));
    }

    public <T> T fromJson(String json, Class<T> type) {
        return gson.fromJson(json, type);
    }

    public ContentResponse get(String targetUrl) throws Exception {
        return send(HttpMethod.GET, targetUrl, Collections.<String, String>emptyMap(), null);
    }

    private ContentResponse send(HttpMethod method, String targetUrl, Map<String, String> headers, String body)
            throws Exception {
        Request request = httpClient.newRequest(targetUrl).method(method);

        for (Map.Entry<String, String> header : headers.entrySet()) {
            request.header(header.getKey(), header.getValue());
        }

        if (body != null) {
            request.content(new StringContentProvider("application/json", body, StandardCharsets.UTF_8));
        }

        return request.send();
    }

    @Override
    public void close() throws Exception {
        if (httpClient.isStarted()) {
            httpClient.stop();
        }
    }
}

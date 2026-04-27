package purejava;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import purejava.file.PersonJsonFileService;
import purejava.file.TextFileService;
import purejava.http.SimpleRestClient;
import purejava.http.SimpleRestServer;
import purejava.model.Person;

public class AppMain {
    public static void main(String[] args) {
        Path textFile = Path.of("input.txt");
        Path jsonFile = Path.of("person.json");

        TextFileService textFileService = new TextFileService();
        PersonJsonFileService personJsonFileService = new PersonJsonFileService();
        SimpleRestClient restClient = new SimpleRestClient();
        SimpleRestServer server = null;

        try {
            List<String> lines = textFileService.readLines(textFile);
            Map<String, String> keyValuePairs = textFileService.readKeyValuePairs(textFile);
            Person person = personJsonFileService.read(jsonFile);

            System.out.println("[FILE] raw lines");
            lines.forEach(System.out::println);

            System.out.println("[FILE] parsed map");
            System.out.println(keyValuePairs);

            System.out.println("[JSON] parsed person");
            System.out.println(person);

            server = new SimpleRestServer(8080);
            server.start();
            System.out.println("[HTTP] server started on port 8080");

            String getResponse = restClient.get("http://127.0.0.1:8080/api/echo?source=file&count=" + lines.size());
            System.out.println("[HTTP] GET response");
            System.out.println(getResponse);

            String postResponse = restClient.postJson("http://127.0.0.1:8080/api/echo", person.toJson());
            System.out.println("[HTTP] POST response");
            System.out.println(postResponse);
        } catch (Exception exception) {
            System.err.println("Application error: " + exception.getMessage());
            exception.printStackTrace();
        } finally {
            if (server != null) {
                server.stop(0);
            }
        }
    }
}

package src.UI;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class WeatherApp {
    private static final String API_KEY = apiKey();
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather";

    public static String run() {
        String[] cities = {"Maidstone"};
        for (String city : cities) {
            String urlString = String.format("%s?q=%s&appid=%s&units=metric", BASE_URL, city, API_KEY);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlString))
                    .build();

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenApply(WeatherApp::parse)
                    .join();
        }
        return "";
    }

    public static String parse(String responseBody) {
        JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();

        String cityName = getStringValue(jsonObject, "name");

        JsonObject main = jsonObject.getAsJsonObject("main");
        double temperature = main != null ? main.get("temp").getAsDouble() : Double.NaN;
        int humidity = main != null ? main.get("humidity").getAsInt() : -1;

        JsonElement weatherElement = jsonObject.getAsJsonArray("weather").get(0);
        JsonObject weather = weatherElement != null ? weatherElement.getAsJsonObject() : null;
        String description = getStringValue(weather, "description");

        System.out.printf("City: %s%nTemperature: %.2f°C%nHumidity: %d%%%nDescription: %s%n",
                cityName, temperature, humidity, description);

        return null;
    }

    private static String getStringValue(JsonObject jsonObject, String key) {
        return jsonObject != null && jsonObject.has(key) ? jsonObject.get(key).getAsString() : "N/A";
    }

    private static String apiKey() {
        String file = "api_key";
        String line = "";
        try (BufferedReader r = new BufferedReader(new FileReader(file))) {
            line = r.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println(line);
        if (line == null) {
            System.err.println("Remember to make a file called api_file and in the first line paste your api key.");
        }
        return line;
    }
}

package app.controller;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;
import java.util.StringJoiner;

@RestController
public class AppController {

    HttpClientUtil httpClientUtil;

    @RequestMapping("/")
    public String getInfo() throws IOException, InterruptedException, ParseException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://opendata.arcgis.com/datasets/372795642c7c45c791b414f4059796df_0.geojson"))
                .build();

        HttpResponse<String> response = HttpClientUtil.httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        JSONParser jsonParser = new JSONParser();
        JSONObject object = (JSONObject) jsonParser.parse(response.body());
        StringJoiner joiner = new StringJoiner("<br>");
        joiner.add(object.get("name").toString());
        JSONArray array = (JSONArray) object.get("features");
        array.forEach(t -> {
            JSONObject o = (JSONObject) ((JSONObject) t).get("properties");
            joiner.add(Optional.ofNullable(o.get("Place").toString()).orElse(""));
            joiner.add("מתאריך: " + Optional.ofNullable(o.get("fromTime").toString()).orElse(""));
            joiner.add("עד תאריך: " + Optional.of(o.get("toTime").toString() + "<br>").orElse(""));
        });

        return joiner.toString();
    }
}

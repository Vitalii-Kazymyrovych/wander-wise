package wander.wise.application.service.api.images;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wander.wise.application.config.ApisConfigProperties;
import wander.wise.application.constants.GlobalConstants;
import wander.wise.application.exception.custom.ImageSearchServiceException;

import static wander.wise.application.constants.GlobalConstants.SET_DIVIDER;

@Service
@RequiredArgsConstructor
public class ImageSearchApiServiceImpl implements ImageSearchApiService {
    private static final String ASPECT = "Wide";
    private static final String HOST = "https://api.bing.microsoft.com";
    private static final String IMAGE_TYPE = "Photo";
    private static final String MIN_HEIGHT = "720";
    private static final String MIN_WIDTH = "1280";
    private static final String PATH = "/v7.0/images/search";
    public static final int IMAGES_PER_CARD_AMOUNT = 5;
    public static final int HEADER_VALUE_INDEX = 0;
    private final ApisConfigProperties apisConfigProperties;

    @Override
    public String getImageLinks(String searchKey) {
        // construct the search request URL (in the form of endpoint + query string)
        String queryParams = buildQueryParams(searchKey);
        HttpURLConnection connection = getHttpUrlConnection(queryParams);
        // receive JSON body
        JsonObject jsonObject = getImagesJsonObject(connection);
        // get the first 10 image results from the JSON object
        return getImageLinksFromJsonBody(jsonObject);
    }

    private static String getImageLinksFromJsonBody(JsonObject jsonObject) {
        JsonArray results = jsonObject.getAsJsonArray("value");
        String[] links = results.asList()
                .stream()
                .map(JsonElement::getAsJsonObject)
                .map(object -> object.get("contentUrl").getAsString())
                .limit(IMAGES_PER_CARD_AMOUNT)
                .toArray(String[]::new);
        return String.join(SET_DIVIDER, links);
    }

    private static String buildQueryParams(String searchKey) {
        String queryParams = "?q=" + URLEncoder.encode(searchKey, StandardCharsets.UTF_8)
                + "&aspect=" + URLEncoder.encode(ASPECT, StandardCharsets.UTF_8)
                + "&minHeight=" + URLEncoder.encode(MIN_HEIGHT, StandardCharsets.UTF_8)
                + "&minWidth=" + URLEncoder.encode(MIN_WIDTH, StandardCharsets.UTF_8)
                + "&imageType=" + URLEncoder.encode(IMAGE_TYPE, StandardCharsets.UTF_8);
        return queryParams;
    }

    private static JsonObject getImagesJsonObject(HttpURLConnection connection) {
        InputStream stream = null;
        SearchResults searchResults = null;
        try {
            stream = connection.getInputStream();
            String response = new Scanner(stream).useDelimiter("\\A").next();
            // construct result object for return
            searchResults = new SearchResults(new HashMap<String, String>(), response);
            // extract Bing-related HTTP headers
            Map<String, List<String>> headers = connection.getHeaderFields();
            for (String header : headers.keySet()) {
                if (header == null) {
                    continue;
                }
                if (header.startsWith("BingAPIs-") || header.startsWith("X-MSEdge-")) {
                    searchResults.relevantHeaders.put(header, headers.get(header).get(HEADER_VALUE_INDEX));
                }
            }
            connection.disconnect();
            stream.close();
        } catch (IOException e) {
            throw new ImageSearchServiceException("Exception occurred when try "
                    + "to get inputStream from connection: "
                    + connection, e);
        }
        JsonObject jsonObject = JsonParser.parseString(searchResults.jsonResponse).getAsJsonObject();
        return jsonObject;
    }

    private HttpURLConnection getHttpUrlConnection(String queryParams) {
        URL url;
        HttpURLConnection connection;
        try {
            url = new URL(HOST + PATH + queryParams);
        } catch (MalformedURLException e) {
            throw new ImageSearchServiceException(
                    "Invalid host: " + HOST
                            + "\nor path: " + PATH
                            + "\nor queryParams: " + queryParams, e);
        }
        try {
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            throw new ImageSearchServiceException("Exception occurred when try "
                    + "to open connection with url: "
                    + url, e);
        }
        connection.setRequestProperty(
                "Ocp-Apim-Subscription-Key",
                apisConfigProperties.imageServiceApiKey());
        return connection;
    }

    @Data
    static class SearchResults {
        private HashMap<String, String> relevantHeaders;
        private String jsonResponse;

        SearchResults(HashMap<String, String> relevantHeaders,
                      String jsonResponse) {
            this.relevantHeaders = relevantHeaders;
            this.jsonResponse = jsonResponse;
        }
    }
}

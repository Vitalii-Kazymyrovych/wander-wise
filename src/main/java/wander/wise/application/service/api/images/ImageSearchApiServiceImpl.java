package wander.wise.application.service.api.images;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import wander.wise.application.config.ApisConfigProperties;
import wander.wise.application.constants.GlobalConstants;
import wander.wise.application.exception.custom.ImageSearchServiceException;

import static wander.wise.application.constants.GlobalConstants.SET_DIVIDER;

@Service
@RequiredArgsConstructor
public class ImageSearchApiServiceImpl implements ImageSearchApiService {
    private RestTemplate restTemplate = new RestTemplate();
    private static final String baseUrl = "https://www.bing.com/images/search?q=";
    private static final String paramsUrl = "&qft=+filterui:aspect-wide+filterui:imagesize-large&form=IRFLTR&first=1";

    @Override
    public String getImageLinks(String searchKey) {
        String searchUrl = baseUrl + URLEncoder.encode(searchKey, StandardCharsets.UTF_8) + paramsUrl;
        return String.join(SET_DIVIDER, getUrls(searchUrl));
    }

    private List<String> getUrls(String searchUrl) {
        String response = "";
        response = restTemplate.getForObject(searchUrl, String.class);

        // Print the response
        String regex = "<li data-idx(?s).*?data-fnvg=";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(response);

        List<String> extractedItems = new ArrayList<>();
        while (matcher.find()) {
            String item = matcher.group(); // Extract the content between <li> tags
            extractedItems.add(item);
        }

        regex = "https://[^\\s]+\\.jpg";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(response.toString());

        List<String> links = new ArrayList<>();
        while (matcher.find()) {
            String link = matcher.group(); // Extract the content between <li> tags
            links.add("https" + link.substring(link.lastIndexOf(":"), link.lastIndexOf("g") + 1));
        }
        return links.stream().limit(15).toList();
    }
}

package wander.wise.application.service.api.maps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import wander.wise.application.config.ApisConfigProperties;
import wander.wise.application.dto.maps.LocationDto;
import wander.wise.application.exception.custom.MapsServiceException;

import static wander.wise.application.constants.GlobalConstants.JSON_MAPPER;

@Service
@RequiredArgsConstructor
public class MapsApiServiceImpl implements MapsApiService {
    private static final String GEOCODING_API_URL = "https://maps.googleapis.com/maps/api/geocode/json";
    private static final double SCALE = Math.pow(10, 6);
    public static final int LON_INDEX = 1;
    public static final int LAT_INDEX = 0;
    private final ApisConfigProperties apisConfigProperties;

    @Override
    public LocationDto getLocationDtoByName(String searchKey) {
        String formattedKey = searchKey.replaceAll(" ", "+");
        ResponseEntity<String> response = getGeocodingResponse(formattedKey);
        JsonNode root = geocodingResponseToJson(response);
        return getLocationDto(formattedKey, root);
    }

    @Override
    public LocationDto getMapsResponseByUsersUrl(String usersUrl) {
        String longUrl = parseLongUrl(usersUrl);
        return longUrlToLocationDto(usersUrl, longUrl);
    }

    private String parseLongUrl(String usersUrl) {
        String longUrl = null;
        try {
            longUrl = getRedirectUrl(usersUrl);
        } catch (IOException e) {
            throw new MapsServiceException("Can't parse full url from the short one: "
                    + usersUrl, e);
        }
        return longUrl;
    }

    private String getRedirectUrl(String usersUrl) throws IOException {
        InputStream inputStream = null;
        try {
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
            URL url = new URL(usersUrl);
            HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
            https.setInstanceFollowRedirects(true);
            HttpsURLConnection.setFollowRedirects(true);
            https.connect();
            int responseCode = https.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inputStream = https.getInputStream();
                return https.getURL().toString();
            }
        } catch (IOException e) {
            throw new MapsServiceException("Something went wrong when parsing long url", e);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return null;
    }

    private ResponseEntity<String> getGeocodingResponse(String formattedKey) {
        String geocodingLink = GEOCODING_API_URL
                + "?address=" + formattedKey
                + "&key=" + apisConfigProperties.mapsApiKey();
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForEntity(geocodingLink, String.class);
    }

    private static LocationDto getLocationDto(
            String formattedKey, JsonNode root) {
        String mapLink = "https://maps.google.com/maps?q=" + formattedKey;
        JsonNode location = root.path("results").path(0).path("geometry").path("location");
        return new LocationDto(mapLink,
                corToDouble(location.path("lat").asText()),
                corToDouble(location.path("lng").asText()));
    }

    private static JsonNode geocodingResponseToJson(ResponseEntity<String> response) {
        JsonNode mapsResponseJson;
        try {
            mapsResponseJson = JSON_MAPPER.readTree(response.getBody());
        } catch (JsonProcessingException e) {
            throw new MapsServiceException("Exception occurred when trying to map response to json: "
                    + response, e);
        }
        return mapsResponseJson;
    }

    private static String[] setCors(String longUrl) {
        String[] coordinatesArray;
        if (longUrl.contains("@")) {
            int atSignIndex = longUrl.indexOf('@');
            String coordinatesString = longUrl.substring(atSignIndex + 1);
            coordinatesArray = coordinatesString.split(",");
        } else {
            int questionMarkIndex = longUrl.indexOf("?");
            String coordinatesString = longUrl.substring(35, questionMarkIndex);
            coordinatesArray = coordinatesString.split(",");
        }
        if (coordinatesArray[LON_INDEX].contains("+")) {
            coordinatesArray[LON_INDEX] = coordinatesArray[LON_INDEX]
                    .replace("+", "");
        }
        return coordinatesArray;
    }

    private static double corToDouble(String corString) {
        if (!corString.isEmpty()) {
            return Math.floor(Double.parseDouble(corString) * SCALE) / SCALE;
        } else {
            return 0;
        }
    }

    private static LocationDto longUrlToLocationDto(String usersUrl, String longUrl) {
        if (longUrl != null) {
            String[] cors = setCors(longUrl);
            return new LocationDto(longUrl,
                    corToDouble(cors[LAT_INDEX]),
                    corToDouble(cors[LON_INDEX]));
        } else {
            throw new MapsServiceException("An error occurred when parsing maps url "
                    + usersUrl
                    + ". Please check the url and try again.");
        }
    }
}

package wander.wise.application.service.api;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;
import wander.wise.application.service.api.images.ImageSearchApiServiceImpl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ImageSearchApiServiceTest {
    public static final String URL = "https://www.bing.com/images"
            + "/search?q=&qft=+filterui:aspect-wide+filterui"
            + ":imagesize-large&form=IRFLTR&first=1";
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ImageSearchApiServiceImpl imageSearchApiService;

    @Test
    void testGetImageLinks_withValidData() throws IOException, NoSuchMethodException {
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn("");
        imageSearchApiService.getImageLinks("");
    }
}
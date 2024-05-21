package wander.wise.application.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("api")
public record ApisConfigProperties(String imageServiceApiKey, String mapsApiKey) {
}

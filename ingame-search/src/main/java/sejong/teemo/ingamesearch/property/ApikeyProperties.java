package sejong.teemo.ingamesearch.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "riot")
public record ApikeyProperties(String apiKey) {
}

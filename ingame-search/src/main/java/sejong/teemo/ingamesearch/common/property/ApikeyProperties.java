package sejong.teemo.ingamesearch.common.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "riot")
public record ApikeyProperties(String apiKey) {
}

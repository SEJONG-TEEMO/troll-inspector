package sejong.teemo.riotapi.common.properties;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "riot")
public record RiotApiProperties(@NotNull String apiKey) {
}

package sejong.teemo.ingamesearch.extension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.test.context.ActiveProfiles;
import sejong.teemo.ingamesearch.dto.Account;
import sejong.teemo.ingamesearch.dto.Spectator;
import sejong.teemo.ingamesearch.property.ApikeyProperties;

import java.util.List;

@EnableConfigurationProperties(ApikeyProperties.class)
@ActiveProfiles("test")
@RestClientTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class TestExtension {

    protected String getAccount() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(new Account("PUUID", "a1h", "KR1"));
    }

    protected String getSpectator() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(new Spectator("type", List.of()));
    }
}

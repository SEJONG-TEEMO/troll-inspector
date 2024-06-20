package sejong.teemo.ingamesearch.container;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import sejong.teemo.ingamesearch.ingame.dto.SpectatorDto;
import sejong.teemo.ingamesearch.ingame.dto.summoner.SummonerPerformance;
import sejong.teemo.ingamesearch.ingame.dto.user.Account;
import sejong.teemo.ingamesearch.ingame.dto.user.LeagueEntryDto;
import sejong.teemo.ingamesearch.ingame.dto.user.SummonerDto;

import java.util.List;

@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class TestContainer {

    private static final String MYSQL_VERSION = "mysql:8.2.0";

    @Container
    private static final MySQLContainer<?> mySqlContainer = new MySQLContainer<>(MYSQL_VERSION);

    static {
        mySqlContainer.withUsername("test");
        mySqlContainer.withPassword("test");
    }
}

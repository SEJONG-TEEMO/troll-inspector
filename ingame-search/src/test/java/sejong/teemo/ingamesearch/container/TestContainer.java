package sejong.teemo.ingamesearch.container;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

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

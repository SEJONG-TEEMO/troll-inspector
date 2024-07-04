package sejong.teemo.batch.container;

import net.datafaker.Faker;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import sejong.teemo.batch.domain.entity.UserInfo;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

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

    protected List<UserInfo> dataInit() {

        Faker faker = new Faker();

        return IntStream.range(0, 100)
                .mapToObj(i -> UserInfo.of(
                        faker.gameOfThrones().character(),
                        faker.lorem().word(),
                        faker.internet().uuid(),
                        faker.internet().uuid(),
                        faker.options().option("Solo", "Duo", "Flex"),
                        faker.options().option("IRON", "BRONZE", "SILVER", "GOLD", "PLATINUM", "EMERALD", "DIAMOND", "MASTER", "GRAND_MASTER", "CHALLENGER"),
                        faker.options().option("I", "II", "III", "IV"),
                        faker.number().numberBetween(0, 500),
                        faker.number().numberBetween(0, 500),
                        faker.number().numberBetween(0, 1000),
                        faker.internet().uuid(),
                        faker.number().numberBetween(0, 1000),
                        ThreadLocalRandom.current().nextLong(1000000000L, 2000000000L),
                        faker.number().numberBetween(1, 500)
                ))
                .toList();
    }
}

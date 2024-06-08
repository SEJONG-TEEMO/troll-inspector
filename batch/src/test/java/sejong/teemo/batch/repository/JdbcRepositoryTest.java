package sejong.teemo.batch.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import sejong.teemo.batch.container.TestContainer;
import sejong.teemo.batch.entity.UserInfo;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class JdbcRepositoryTest extends TestContainer {

    @Autowired
    private JdbcRepository jdbcRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void 유저_정보_벌크_연산_테스트() {
        // given
        UserInfo userInfo = UserInfo.of(
                "alh",
                "a",
                "a",
                "a",
                "a", "a",
                "a",
                1,
                1,
                1,
                "a",
                1,
                1L,
                1L
        );

        List<UserInfo> list = List.of(userInfo);

        // when

        // then
        assertThatCode(() -> jdbcRepository.bulkInsert(list))
                .doesNotThrowAnyException();
    }
}

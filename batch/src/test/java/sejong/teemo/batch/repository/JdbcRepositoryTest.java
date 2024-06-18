package sejong.teemo.batch.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import sejong.teemo.batch.container.TestContainer;
import sejong.teemo.batch.entity.TempUserInfo;
import sejong.teemo.batch.entity.UserInfo;
import sejong.teemo.batch.repository.mapper.TempUserInfoRowMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Sql("/data-test.sql")
public class JdbcRepositoryTest extends TestContainer {

    @Autowired
    private BatchRepository batchRepository;

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
        assertThatCode(() -> batchRepository.bulkInsert(list))
                .doesNotThrowAnyException();
    }

    @Test
    void 벌크_인서트_후_FIND_ALL_하여_유저가_존재하는지_확인_테스트_컨테이너_데이터_무결성() {
        // given

        // when
        List<TempUserInfo> list = jdbcTemplate.query("select * from tmp_user_info", new TempUserInfoRowMapper());

        // then
        System.out.println(list.getFirst().getGameName());
        assertThat(list).hasSize(50);
    }
}

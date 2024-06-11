package sejong.teemo.batch.item.writer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import sejong.teemo.batch.entity.UserInfo;
import sejong.teemo.batch.repository.JdbcRepository;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class LeagueItemWriter implements ItemWriter<List<UserInfo>> {

    private final JdbcRepository jdbcRepository;

    @Override
    public void write(Chunk<? extends List<UserInfo>> chunk) throws Exception {
        chunk.forEach(jdbcRepository::bulkInsert);
    }
}

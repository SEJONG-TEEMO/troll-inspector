package sejong.teemo.batch.infrastructure.item.writer;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import sejong.teemo.batch.domain.entity.TempUserInfo;
import sejong.teemo.batch.domain.repository.JdbcRepository;

@RequiredArgsConstructor
@Slf4j
public class LeagueItemWriter implements ItemWriter<List<TempUserInfo>> {

    private final JdbcRepository jdbcRepository;

    @Override
    public void write(Chunk<? extends List<TempUserInfo>> chunk) throws Exception {
        log.info("Writing league data");
        chunk.forEach(jdbcRepository::bulkInsertTemp);
    }
}

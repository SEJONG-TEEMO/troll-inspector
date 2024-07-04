package sejong.teemo.batch.application.item.writer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import sejong.teemo.batch.domain.entity.TempUserInfo;
import sejong.teemo.batch.domain.repository.JdbcRepository;

import java.util.List;

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

package sejong.teemo.batch.infrastructure.item.reader;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import sejong.teemo.batch.domain.dto.LeagueEntryDto;
import sejong.teemo.batch.infrastructure.external.BatchExternalApi;

@Slf4j
public class LeagueItemReader implements ItemReader<List<LeagueEntryDto>> {

    private final BatchExternalApi batchExternalApi;
    private final String tier;
    private final String division;

    public LeagueItemReader(BatchExternalApi batchExternalApi, String tier, String division) {
        this.batchExternalApi = batchExternalApi;
        this.tier = tier;
        this.division = division;
    }

    private int page;
    private static final String queue = "RANKED_SOLO_5x5";

    @Override
    public List<LeagueEntryDto> read() throws Exception {
        page++;

        log.info("{}-{} page = {}", tier, division, page);

        List<LeagueEntryDto> leagueEntryDtos = batchExternalApi.callRiotLeague(division, tier, queue, page);

        if (leagueEntryDtos.isEmpty()) {
            log.info("{}-{} finish!!", tier, division);
            return null;
        }

        return leagueEntryDtos;
    }
}

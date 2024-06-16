package sejong.teemo.batch.item.reader;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import sejong.teemo.batch.dto.LeagueEntryDto;
import sejong.teemo.batch.service.BatchService;

import java.util.List;

@Slf4j
public class LeagueItemReader implements ItemReader<List<LeagueEntryDto>> {

    private final BatchService batchService;
    private final String tier;
    private final String division;

    public LeagueItemReader(BatchService batchService, String tier, String division) {
        this.batchService = batchService;
        this.tier = tier;
        this.division = division;
    }

    private int page;
    private static final String queue = "RANKED_SOLO_5x5";

    @Override
    public List<LeagueEntryDto> read() throws Exception {
        page++;

        log.info("{}-{} page = {}", tier, division, page);

        List<LeagueEntryDto> leagueEntryDtos = batchService.callRiotLeague(division, tier, queue, page);

        if (leagueEntryDtos.isEmpty()) {
            log.info("{}-{} finish!!", tier, division);
            return null;
        }

        return leagueEntryDtos;
    }
}

package sejong.teemo.batch.item.reader;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import sejong.teemo.batch.dto.UserInfoDto;
import sejong.teemo.batch.service.BatchService;

import java.util.List;

@Slf4j
public class LeagueItemReader implements ItemReader<List<UserInfoDto>> {

    private final BatchService batchService;
    private final Integer maxPage;
    private final String tier;
    private final String division;

    public LeagueItemReader(BatchService batchService, Integer maxPage, String tier, String division) {
        this.batchService = batchService;
        this.maxPage = maxPage;
        this.tier = tier;
        this.division = division;
    }

    private int page;
    private static final String queue = "RANKED_SOLO_5x5";

    @Override
    public List<UserInfoDto> read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        page++;

        if(page > maxPage) {
            return List.of();
        }

        Thread.sleep(9000L);
        log.info("page = {}", page);
        return batchService.callApiUserInfo(division, tier, queue, page);
    }
}

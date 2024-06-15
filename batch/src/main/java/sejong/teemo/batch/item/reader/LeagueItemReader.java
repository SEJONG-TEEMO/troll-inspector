package sejong.teemo.batch.item.reader;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import sejong.teemo.batch.dto.UserInfoDto;
import sejong.teemo.batch.exception.ExceptionProvider;
import sejong.teemo.batch.exception.FailedRetryException;
import sejong.teemo.batch.service.BatchService;

import java.util.List;

@Slf4j
public class LeagueItemReader implements ItemReader<List<UserInfoDto>> {

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
    public List<UserInfoDto> read() throws Exception {
        page++;

        log.info("{}-{} page = {}", tier, division, page);

        Thread.sleep(15000L);

        try {
            List<UserInfoDto> userInfoDtos = batchService.callApiUserInfo(division, tier, queue, page);

            if (userInfoDtos.isEmpty()) {
                log.info("{}-{} finish!!", tier, division);
                return null;
            }

            return userInfoDtos;
        } catch (FailedRetryException e) {
            log.error("{}-{} failed = {}", tier, division, e.getMessage());
            throw new FailedRetryException(ExceptionProvider.RETRY_FAILED);
        }
    }
}

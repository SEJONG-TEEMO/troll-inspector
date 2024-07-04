package sejong.teemo.batch.application.item.process;

import io.github.bucket4j.Bucket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import sejong.teemo.batch.common.async.AsyncCall;
import sejong.teemo.batch.presentation.dto.Account;
import sejong.teemo.batch.presentation.dto.LeagueEntryDto;
import sejong.teemo.batch.presentation.dto.SummonerDto;
import sejong.teemo.batch.presentation.dto.UserInfoDto;
import sejong.teemo.batch.domain.entity.TempUserInfo;
import sejong.teemo.batch.common.exception.ExceptionProvider;
import sejong.teemo.batch.common.exception.FailedApiCallingException;
import sejong.teemo.batch.application.service.BatchService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class LeagueItemProcess implements ItemProcessor<List<LeagueEntryDto>, List<TempUserInfo>> {

    private final BatchService batchService;
    private final Bucket bucket;

    @Override
    public List<TempUserInfo> process(List<LeagueEntryDto> item) throws FailedApiCallingException, InterruptedException {

        log.info("batch process");

        AsyncCall<LeagueEntryDto, TempUserInfo> asyncCall = new AsyncCall<>(item);

        return asyncCall.execute(10, this::getUserInfo);
    }

    private TempUserInfo getUserInfo(LeagueEntryDto leagueEntryDto) throws FailedApiCallingException {

        try {

            bucket.asBlocking().consume(1);

            SummonerDto summonerDto = batchService.callApiSummoner(leagueEntryDto.summonerId());
            Account account = batchService.callRiotAccount(summonerDto.puuid());

            UserInfoDto userInfoDto = UserInfoDto.of(leagueEntryDto, summonerDto, account);

            return TempUserInfo.from(userInfoDto);
        } catch (Exception e) {
            throw new FailedApiCallingException(ExceptionProvider.RIOT_API_MODULE_USER_INFO_FAILED);
        }
    }
}

package sejong.teemo.batch.item.process;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import sejong.teemo.batch.async.AsyncCall;
import sejong.teemo.batch.dto.Account;
import sejong.teemo.batch.dto.LeagueEntryDto;
import sejong.teemo.batch.dto.SummonerDto;
import sejong.teemo.batch.dto.UserInfoDto;
import sejong.teemo.batch.entity.UserInfo;
import sejong.teemo.batch.exception.ExceptionProvider;
import sejong.teemo.batch.exception.FailedApiCallingException;
import sejong.teemo.batch.service.BatchService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class LeagueItemProcess implements ItemProcessor<List<LeagueEntryDto>, List<UserInfo>> {

    private final BatchService batchService;

    @Override
    public List<UserInfo> process(List<LeagueEntryDto> item) throws FailedApiCallingException, InterruptedException {

        log.info("batch process");

        AsyncCall<LeagueEntryDto, UserInfo> asyncCall = new AsyncCall<>(item);

        List<UserInfo> execute = asyncCall.execute(10, this::getUserInfo);

        Thread.sleep(10000);

        return execute;
    }

    private UserInfo getUserInfo(LeagueEntryDto leagueEntryDto) throws FailedApiCallingException {

        try {
            SummonerDto summonerDto = batchService.callApiSummoner(leagueEntryDto.summonerId());
            Account account = batchService.callRiotAccount(summonerDto.puuid());

            UserInfoDto userInfoDto = UserInfoDto.of(leagueEntryDto, summonerDto, account);

            return UserInfo.from(userInfoDto);
        } catch (Exception e) {
            throw new FailedApiCallingException(ExceptionProvider.RIOT_API_MODULE_USER_INFO_FAILED);
        }
    }
}

package sejong.teemo.riotapi.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sejong.teemo.riotapi.async.AsyncCall;
import sejong.teemo.riotapi.dto.LeagueEntryDto;
import sejong.teemo.riotapi.dto.SummonerDto;
import sejong.teemo.riotapi.facade.annotation.Facade;
import sejong.teemo.riotapi.service.LeagueService;
import sejong.teemo.riotapi.service.SummonerService;

import java.util.List;

@Facade
@RequiredArgsConstructor
@Slf4j
public class LeagueFacade {
    
    private final LeagueService leagueService;
    private final SummonerService summonerService;
    
    public List<SummonerDto> callApiLeagueToSummoner(String division, String tier, String queue, int page) {
        List<LeagueEntryDto> leagueEntryDtos = leagueService.callRiotLeague(division, tier, queue, page);

        AsyncCall<LeagueEntryDto, SummonerDto> asyncCall = new AsyncCall<>(leagueEntryDtos);
        return asyncCall.asyncCallApi(10, leagueEntryDto ->
                summonerService.callApiSummoner(leagueEntryDto.summonerId())
        );
    }
}

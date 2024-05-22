package sejong.teemo.trollinspector.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import sejong.teemo.trollinspector.domain.Account;
import sejong.teemo.trollinspector.domain.SummonerPerformance;
import sejong.teemo.trollinspector.repository.SummonerPerformanceRepository;
import sejong.teemo.trollinspector.util.ConfigProperties;

import java.util.ArrayList;
import java.util.List;

import static sejong.teemo.trollinspector.util.parsing.JsonToPlayerPerformance.jsonToPlayerPerformance;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchService {

    private final RestClient restClient;
    private final ConfigProperties configProperties;
    private final SummonerPerformanceRepository summonerPerformanceRepository;

    private final int PAGE_SIZE = 20;

    public List<SummonerPerformance> getItemByUsername(String username) {
        return summonerPerformanceRepository.findByUsername(username);
    }

    public List<SummonerPerformance> analyzeSummonerPerformance(String gameName, String tagLine) throws Exception {
        try {
            Account account = getPuuid(gameName, tagLine);
            String username = String.format("%s#%s", gameName, tagLine);

            List<String> matchIds = getMatchIds(account.puuid());

            List<SummonerPerformance> matchDetails = new ArrayList<>();
            for (String matchId : matchIds) {
                String matchDetail = getMatchDetail(matchId);

                matchDetails.add(jsonToPlayerPerformance(matchDetail, username, account.puuid()));
            }

            log.info(String.valueOf(account));
            log.info(String.valueOf(matchIds));

            storeToElasticsearch(matchDetails);

            return matchDetails;

        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new Exception("error");
    }

    private Account getPuuid(String gameName, String tagLine) {

        return this.restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/riot/account/v1/accounts/by-riot-id/{gameName}/{tagLine}")
                        .queryParam("api_key", configProperties.riot().apikey())
                        .build(gameName, tagLine))
                .retrieve()
                .body(Account.class);
    }

    private List<String> getMatchIds(String puuid) {
        return this.restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/lol/match/v5/matches/by-puuid/{puuid}/ids")
                        .queryParam("start", 0)
                        .queryParam("count", PAGE_SIZE)
                        .queryParam("api_key", configProperties.riot().apikey())
                        .build(puuid))
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    private String getMatchDetail(String matchId) {
        return this.restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/lol/match/v5/matches/{matchId}")
                        .queryParam("api_key", configProperties.riot().apikey())
                        .build(matchId))
                .retrieve()
                .body(String.class);
    }

    private void storeToElasticsearch(List<SummonerPerformance> performances) {
        summonerPerformanceRepository.saveAll(performances);
    }
}


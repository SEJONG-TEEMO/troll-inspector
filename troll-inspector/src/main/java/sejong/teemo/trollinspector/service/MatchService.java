package sejong.teemo.trollinspector.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import sejong.teemo.trollinspector.domain.Account;
import sejong.teemo.trollinspector.domain.SummonerPerformance;
import sejong.teemo.trollinspector.repository.SummonerPerformanceRepository;

import java.util.ArrayList;
import java.util.List;

import static sejong.teemo.trollinspector.util.parsing.JsonToPlayerPerformance.jsonToPlayerPerformance;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchService {
    private final RestClient restClient;
    private final String API_KEYS = "RGAPI-4dd07247-61f0-425b-a223-a9b2df6499ec";
    private final int PAGE_SIZE = 20;
    private final SummonerPerformanceRepository summonerPerformanceRepository;

    public List<SummonerPerformance> getItemByUsername(String username) {
        return summonerPerformanceRepository.findByUsername(username);
    }

    public List<SummonerPerformance> analyzeSummonerPerformance(String gameName, String tagLine) throws Exception {
        try {
            // 1단계: getPuuid로 puuid 가져오기
            Account account = getPuuid(gameName, tagLine);
            String username = String.format("%s#%s", gameName, tagLine);

            // 2단계: getMatchIds로 매치 ID 목록 가져오기
            List<String> matchIds = getMatchIds(account.puuid());

            // 3단계: 각 매치 ID에 대한 상세 정보 가져오기
            List<SummonerPerformance> matchDetails = new ArrayList<>();
            for (String matchId : matchIds) {
                String matchDetail = getMatchDetail(matchId);

                matchDetails.add(jsonToPlayerPerformance(matchDetail, username, account.puuid()));
            }
            log.info(String.valueOf(account));
            log.info(String.valueOf(matchIds));

            // matchDetails 처리 로직
            // 예: matchDetails.forEach(System.out::println);
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
                        .queryParam("api_key", API_KEYS)
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
                        .queryParam("api_key", API_KEYS)
                        .build(puuid))
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    private String getMatchDetail(String matchId) {
        return this.restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/lol/match/v5/matches/{matchId}")
                        .queryParam("api_key", API_KEYS)
                        .build(matchId))
                .retrieve()
                .body(String.class);
    }

    private void storeToElasticsearch(List<SummonerPerformance> performances) {
        summonerPerformanceRepository.saveAll(performances);
    }
}


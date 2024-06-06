package sejong.teemo.trollinspector.service;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.vavr.CheckedFunction0;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import sejong.teemo.trollinspector.domain.Account;
import sejong.teemo.trollinspector.domain.SummonerPerformance;
import sejong.teemo.trollinspector.repository.SummonerPerformanceRepository;
import sejong.teemo.trollinspector.util.ConfigProperties;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.stream.Collectors.toList;
import static sejong.teemo.trollinspector.util.parsing.JsonToPlayerPerformance.jsonToPlayerPerformance;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchService {

    private final RateLimiter rateLimiter;
    private final WebClient webClient;
    private final ConfigProperties configProperties;
    private final SummonerPerformanceRepository summonerPerformanceRepository;

    private final int PAGE_SIZE = 20;

    public List<SummonerPerformance> getItemByUsername(String username) {
        return summonerPerformanceRepository.findByUsername(username);
    }

    public List<SummonerPerformance> analyzeSummonerPerformance(String gameName, String tagLine) throws Exception {
        try {
            Account account = getPuuid(gameName, tagLine).block();
            String username = String.format("%s#%s", gameName, tagLine);

            List<String> matchIds = getMatchIds(account.puuid()).block();

            List<CompletableFuture<SummonerPerformance>> futures = matchIds.stream()
                    .map(matchId -> getMatchDetail(matchId)
                            .map(matchDetail -> {
                                SummonerPerformance performance = jsonToPlayerPerformance(matchDetail, username, account.puuid());
                                return storePerformanceToElasticsearch(performance);
                            })
                            .toFuture()).toList();

            return futures.stream()
                    .map(CompletableFuture::join)
                    .collect(toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new Exception("error");
    }

    public List<SummonerPerformance> analyzeSummonerPerformanceForPersonalKey(String gameName, String tagLine) throws Exception {
        try {
            Account account = getPuuid(gameName, tagLine).block();
            String username = String.format("%s#%s", gameName, tagLine);

            List<String> matchIds = getMatchIds(account.puuid()).block();

            List<CompletableFuture<SummonerPerformance>> futures = matchIds.stream()
                    .map(matchId -> getMatchDetailForPersonalKey(matchId)
                            .map(matchDetail -> jsonToPlayerPerformance(matchDetail, username, account.puuid()))
                            .toFuture()).toList();

            List<SummonerPerformance> matchDetails = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                    .thenApply(v -> futures.stream()
                            .map(CompletableFuture::join)
                            .collect(toList()))
                    .join();

            log.info(String.valueOf(account));
            log.info(String.valueOf(matchIds));

            storePerformancesToElasticsearch(matchDetails);

            return matchDetails;

        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new Exception("error");
    }

    private Mono<Account> getPuuid(String gameName, String tagLine) {
        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/riot/account/v1/accounts/by-riot-id/{gameName}/{tagLine}")
                        .queryParam("api_key", configProperties.riot().apikey())
                        .build(gameName, tagLine))
                .retrieve()
                .bodyToMono(Account.class);
    }

    private Mono<List<String>> getMatchIds(String puuid) {
        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/lol/match/v5/matches/by-puuid/{puuid}/ids")
                        .queryParam("start", 0)
                        .queryParam("count", PAGE_SIZE)
                        .queryParam("api_key", configProperties.riot().apikey())
                        .build(puuid))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {
                });
    }

    private Mono<String> getMatchDetail(String matchId) {
        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/lol/match/v5/matches/{matchId}")
                        .queryParam("api_key", configProperties.riot().apikey())
                        .build(matchId))
                .retrieve()
                .bodyToMono(String.class);
    }

    private Mono<String> getMatchDetailForPersonalKey(String matchId) {
        return Mono.defer(() -> {
            CheckedFunction0<String> decoratedSupplier = RateLimiter.decorateCheckedSupplier(rateLimiter, () ->
                    this.webClient.get()
                            .uri(uriBuilder -> uriBuilder
                                    .path("/lol/match/v5/matches/{matchId}")
                                    .queryParam("api_key", configProperties.riot().apikey())
                                    .build(matchId))
                            .retrieve()
                            .bodyToMono(String.class)
                            .block()
            );

            try {
                return Mono.just(decoratedSupplier.apply());
            } catch (Throwable throwable) {
                return Mono.error(throwable);
            }
        });
    }

    private void storePerformancesToElasticsearch(List<SummonerPerformance> performances) {
        summonerPerformanceRepository.saveAll(performances);
    }

    private SummonerPerformance storePerformanceToElasticsearch(SummonerPerformance performance) {
        return summonerPerformanceRepository.save(performance);
    }
}


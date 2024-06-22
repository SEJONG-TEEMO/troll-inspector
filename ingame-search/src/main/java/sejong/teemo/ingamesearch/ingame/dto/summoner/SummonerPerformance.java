package sejong.teemo.ingamesearch.ingame.dto.summoner;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record SummonerPerformance(
        int championId,
        String puuid,
        Double kda,
        Double killParticipation, // 플레이어의 게임의 전체 킬의 비중
        int kills,
        int deaths,
        int assists,
        int totalMinionsKilled,
        int pentaKills,
        int quadraKills,
        int tripleKills,
        boolean wins
) {
}

package sejong.teemo.ingamesearch.domain.dto.champion;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ChampionMastery(
        String puuid,
        long championPointsUntilNextLevel,
        boolean chestGranted,
        long championId,
        long lastPlayTime,
        int championLevel,
        String summonerId,
        int championPoints,
        long championPointsSinceLastLevel,
        int tokensEarned
) {
}

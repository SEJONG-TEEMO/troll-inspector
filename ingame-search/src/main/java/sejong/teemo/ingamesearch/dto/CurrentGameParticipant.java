package sejong.teemo.ingamesearch.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CurrentGameParticipant(
        long championId,
        long profileIconId,
        boolean bot,
        long teamId,
        String summonerId,
        String puuid,
        long spell1Id,
        long spell2Id
) {
}
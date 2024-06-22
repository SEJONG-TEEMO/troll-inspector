package sejong.teemo.riotapi.dto.match;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ParticipantDto(
        int championId,
        String puuid,
        String lane,
        int kills,
        int deaths,
        int assists,
        int physicalDamageDealtToChampions,
        int magicDamageDealtToChampions,
        int detectorWardsPlaced,
        int damageDealtToBuildings,
        int teleportTakedowns,
        int totalMinionsKilled,
        boolean win,
        int pentaKills,
        int quadraKills,
        int tripleKills,
        ChallengesDto challenges
) {
}

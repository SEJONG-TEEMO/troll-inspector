package sejong.teemo.riotapi.dto.match;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ParticipantDto(
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
        ChallengesDto challenges
) {
}

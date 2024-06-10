package sejong.teemo.riotapi.dto.match;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ChallengesDto(
        Double kda,
        int stealthWardsPlaced,
        int wardTakedowns,
        int controlWardsPlaced,
        int dragonTakedowns,
        int baronTakedowns,
        double killParticipation,
        int teleportTakedowns
) {
}

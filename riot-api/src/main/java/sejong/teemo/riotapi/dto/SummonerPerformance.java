package sejong.teemo.riotapi.dto;

import lombok.Builder;
import sejong.teemo.riotapi.dto.match.MatchDto;
import sejong.teemo.riotapi.dto.match.ParticipantDto;

@Builder
public record SummonerPerformance(
        String puuid,
        int targetIndex,
        String username,
        String lane,
        Double kda,
        Double killParticipation,
        int kills,
        int deaths,
        int assists,
        int physicalDamageDealtToChampions,
        int magicDamageDealtToChampions,
        int stealthWardsPlaced,
        int wardTakedowns,
        int controlWardsPlaced,
        int detectorWardsPlaced,
        int damageDealtToBuildings,
        int dragonTakedowns,
        int baronTakedowns,
        int teleportTakedowns,
        int totalMinionsKilled
) {

    public static SummonerPerformance of(int targetIdx, ParticipantDto participantDto, Account account) {
        return SummonerPerformance.builder()
                .puuid(participantDto.puuid())
                .targetIndex(targetIdx)
                .username(account.gameName() + "#" + account.tagLine())
                .lane(participantDto.lane())
                .kda(participantDto.challenges().kda())
                .kills(participantDto.kills())
                .deaths(participantDto.deaths())
                .assists(participantDto.assists())
                .killParticipation(participantDto.challenges().killParticipation())
                .physicalDamageDealtToChampions(participantDto.physicalDamageDealtToChampions())
                .magicDamageDealtToChampions(participantDto.magicDamageDealtToChampions())
                .stealthWardsPlaced(participantDto.challenges().stealthWardsPlaced())
                .wardTakedowns(participantDto.challenges().wardTakedowns())
                .controlWardsPlaced(participantDto.challenges().controlWardsPlaced())
                .detectorWardsPlaced(participantDto.detectorWardsPlaced())
                .damageDealtToBuildings(participantDto.damageDealtToBuildings())
                .dragonTakedowns(participantDto.challenges().dragonTakedowns())
                .baronTakedowns(participantDto.challenges().baronTakedowns())
                .teleportTakedowns(participantDto.teleportTakedowns())
                .totalMinionsKilled(participantDto.totalMinionsKilled())
                .build();
    }
}

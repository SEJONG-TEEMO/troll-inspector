package sejong.teemo.riotapi.domain.summoner.dto;

import lombok.Builder;
import sejong.teemo.riotapi.domain.match.dto.ParticipantDto;
import sejong.teemo.riotapi.domain.match.dto.TeamDto;

@Builder
public record SummonerPerformance(
        int championId,
        String puuid,
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
        int totalMinionsKilled,
        int pentaKills,
        int quadraKills,
        int tripleKills,
        boolean win
) {

    public static SummonerPerformance from(ParticipantDto participantDto) {
        return SummonerPerformance.builder()
                .championId(participantDto.championId())
                .puuid(participantDto.puuid())
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
                .pentaKills(participantDto.pentaKills())
                .quadraKills(participantDto.quadraKills())
                .tripleKills(participantDto.tripleKills())
                .win(participantDto.win())
                .build();
    }

    public static SummonerPerformance of(ParticipantDto participantDto, TeamDto teamDto) {
        return SummonerPerformance.builder()
                .championId(participantDto.championId())
                .puuid(participantDto.puuid())
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
                .pentaKills(participantDto.pentaKills())
                .quadraKills(participantDto.quadraKills())
                .tripleKills(participantDto.tripleKills())
                .win(teamDto.win())
                .build();
    }
}

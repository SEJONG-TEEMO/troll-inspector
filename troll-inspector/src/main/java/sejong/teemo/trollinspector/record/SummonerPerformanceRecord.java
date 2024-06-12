package sejong.teemo.trollinspector.record;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import sejong.teemo.trollinspector.domain.SummonerPerformance;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SummonerPerformanceRecord(String puuid, int targetIndex, String lane, Double kda,
                                        Double killParticipation, int kills, int deaths, int assists,
                                        int physicalDamageDealtToChampions, int magicDamageDealtToChampions,
                                        int stealthWardsPlaced, int wardTakedowns, int controlWardsPlaced,
                                        int detectorWardsPlaced,
                                        int damageDealtToBuildings, int dragonTakedowns, int baronTakedowns,
                                        int teleportTakedowns, int totalMinionsKilled, int championId) {

    public static SummonerPerformance toSummonerPerformance(SummonerPerformanceRecord record) {
        return SummonerPerformance.builder()
                .puuid(record.puuid())
                .targetIndex(record.targetIndex())
                .lane(record.lane())
                .kda(record.kda())
                .killParticipation(record.killParticipation())
                .kills(record.kills())
                .deaths(record.deaths())
                .assists(record.assists())
                .physicalDamageDealtToChampions(record.physicalDamageDealtToChampions())
                .magicDamageDealtToChampions(record.magicDamageDealtToChampions())
                .stealthWardsPlaced(record.stealthWardsPlaced())
                .wardTakedowns(record.wardTakedowns())
                .controlWardsPlaced(record.controlWardsPlaced())
                .detectorWardsPlaced(record.detectorWardsPlaced())
                .damageDealtToBuildings(record.damageDealtToBuildings())
                .dragonTakedowns(record.dragonTakedowns())
                .baronTakedowns(record.baronTakedowns())
                .teleportTakedowns(record.teleportTakedowns())
                .totalMinionsKilled(record.totalMinionsKilled())
                .championId(record.championId())
                .build();
    }
}

package sejong.teemo.trollinspector.record;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SummonerPerformanceRecord(String puuid, int targetIndex, String username, String lane, Double kda,
                                        Double killParticipation, int kills, int deaths, int assists,
                                        int physicalDamageDealtToChampions, int magicDamageDealtToChampions,
                                        int stealthWardsPlaced, int wardTakedowns, int controlWardsPlaced,
                                        int detectorWardsPlaced,
                                        int damageDealtToBuildings, int dragonTakedowns, int baronTakedowns,
                                        int teleportTakedowns, int totalMinionsKilled) {
}

package sejong.teemo.trollinspector.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

@AllArgsConstructor
@Getter
@ToString
@Builder
@Document(indexName = "player_performance")
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class SummonerPerformance {

    @Id
    @Field(name = "id", type = FieldType.Keyword)
    private String id;

    @Field(type = FieldType.Keyword)
    private String puuid;

    @Field(type = FieldType.Integer)
    private int targetIndex;

    @Field(type = FieldType.Keyword)
    private String username;

    @Field(type = FieldType.Keyword)
    private String lane; // 포지션

    @Field(type = FieldType.Double)
    private Double kda;

    @Field(type = FieldType.Double)
    private Double killParticipation; // 킬 관여율

    @Field(type = FieldType.Integer)
    private int kills;

    @Field(type = FieldType.Integer)
    private int deaths;

    @Field(type = FieldType.Integer)
    private int assists;

    @Field(type = FieldType.Integer)
    private int physicalDamageDealtToChampions; // AD 딜량

    @Field(type = FieldType.Integer)
    private int magicDamageDealtToChampions; // AP 딜량

    @Field(type = FieldType.Integer)
    private int stealthWardsPlaced; // 일반 와드 설치

    @Field(type = FieldType.Integer)
    private int wardTakedowns; // 와드 제거 횟수

    @Field(type = FieldType.Integer)
    private int controlWardsPlaced; // 제어 와드 설치

    @Field(type = FieldType.Integer)
    private int detectorWardsPlaced; // 스캔 와드를 사용하여 적 와드를 제거한 횟수

    @Field(type = FieldType.Integer)
    private int damageDealtToBuildings; // 건축물 피격량

    @Field(type = FieldType.Integer)
    private int dragonTakedowns;

    @Field(type = FieldType.Integer)
    private int baronTakedowns;

    @Builder.Default
    @Field(type = FieldType.Integer)
    private int teleportTakedowns = 0; // 텔레포트로 적 처치 관여

    @Field(type = FieldType.Integer)
    private int totalMinionsKilled; // 총 미니언 처치 수

    @Field(type = FieldType.Long)
    private long championId; // 사용한 챔피언

    @Field(type = FieldType.Text)
    private String championName; // 사용한 챔피언

    public static SummonerPerformance createPlayerPerformance(String puuid, int targetIndex, String username, String lane, Double kda, Double killParticipation, int kills, int deaths, int assists,
                                                              int physicalDamageDealtToChampions, int magicDamageDealtToChampions,
                                                              int stealthWardsPlaced, int wardTakedowns, int controlWardsPlaced, int detectorWardsPlaced,
                                                              int damageDealtToBuildings, int dragonTakedowns, int baronTakedowns, int teleportTakedowns,
                                                              int totalMinionsKilled, long championId, String championName) {
        return SummonerPerformance.builder()
                .puuid(puuid)
                .targetIndex(targetIndex)
                .username(username)
                .lane(lane)
                .kda(kda)
                .killParticipation(killParticipation)
                .kills(kills)
                .deaths(deaths)
                .assists(assists)
                .physicalDamageDealtToChampions(physicalDamageDealtToChampions)
                .magicDamageDealtToChampions(magicDamageDealtToChampions)
                .stealthWardsPlaced(stealthWardsPlaced)
                .wardTakedowns(wardTakedowns)
                .controlWardsPlaced(controlWardsPlaced)
                .detectorWardsPlaced(detectorWardsPlaced)
                .damageDealtToBuildings(damageDealtToBuildings)
                .dragonTakedowns(dragonTakedowns)
                .baronTakedowns(baronTakedowns)
                .teleportTakedowns(teleportTakedowns)
                .totalMinionsKilled(totalMinionsKilled)
                .championId(championId)
                .championName(championName)
                .build();
    }
}



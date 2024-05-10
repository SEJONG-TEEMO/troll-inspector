package sejong.teemo.trollinspector.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Builder
@Document(indexName = "player_performance")
public class PlayerPerformance {

    @Id
    @Field(name = "id", type = FieldType.Keyword)
    private String id;

    @Field(type = FieldType.Keyword)
    private String puuid;

    @Field(type = FieldType.Text)
    private String lane; // 포지션

    @Field(type = FieldType.Double)
    private Double kdaRatio;

    @Field(type = FieldType.Double)
    private Double killParticipationRate; // 킬 관여율

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

    @Field(type = FieldType.Integer)
    private int teleportTakedowns; // 텔레포트로 적 처치 관여

    @Field(type = FieldType.Integer)
    private int totalMinionsKilled; // 총 미니언 처치 수

}



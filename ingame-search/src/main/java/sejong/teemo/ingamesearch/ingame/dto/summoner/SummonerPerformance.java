package sejong.teemo.ingamesearch.ingame.dto.summoner;

import lombok.Builder;

@Builder
public record SummonerPerformance(
        int championId,
        String puuid,
        int targetIndex, // 라인에 NONE 인 경우 참가자의 순번을 매겨 NONE의 라인을 예측하는 방법
        String lane, // 라인 -> NONE 인 경우가 많아서 정확한 지표가 될 지 모르겠다.
        Double kda,
        Double killParticipation, // 플레이어의 게임의 전체 킬의 비중
        int kills,
        int deaths,
        int assists,
        int physicalDamageDealtToChampions,// 적 챔피언들에게 가한 물리 피해량
        int magicDamageDealtToChampions, // 적 챔피언들에게 가한 마법 피해량
        int stealthWardsPlaced, // 플레이어가 설치한 일반 와드의 개수
        int wardTakedowns, // 플레이어가 와드를 지운 개수
        int controlWardsPlaced, // 플레이어가 설치한 제어 와드 (핑크 와드)의 개수
        int detectorWardsPlaced, // 위 변수와 동일하다.
        int damageDealtToBuildings, // 플레이어가 적 건물(넥서스, 포탑, 억제기)에 가한 피해량
        int dragonTakedowns, // 드래곤을 처치한 횟 수
        int baronTakedowns, // 바론을 처치한 횟 수
        int teleportTakedowns, // 텔레포트 스펠을 활용한 횟수 -> 로밍을 다닌 횟수로 판단하려고 한다.
        int totalMinionsKilled // 미니언을 얼마나 처치했는지에 대한 횟수
) {
}

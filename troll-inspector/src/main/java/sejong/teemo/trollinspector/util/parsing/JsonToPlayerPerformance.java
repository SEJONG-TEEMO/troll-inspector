package sejong.teemo.trollinspector.util.parsing;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import sejong.teemo.trollinspector.domain.SummonerPerformance;

import java.util.NoSuchElementException;

public class JsonToPlayerPerformance {

    public static SummonerPerformance jsonToPlayerPerformance(String jsonInput, String username, String targetPuuid) {

        final ObjectMapper objectMapper = new ObjectMapper();

        /**
         * metadata : { participants [ 10명의 사용자 ] } 에서 먼저 검색하려는 소환사의 puuid 인덱스 추출
         * info : { participants [ [idx]의 사용자] } 정보 추출
         */

        try {
            JsonNode rootNode = objectMapper.readTree(jsonInput);
            JsonNode metadataParticipantsNode = rootNode.path("metadata").path("participants");
            int targetIndex = -1;

            // metadata에서 puuid 인덱스 추출
            if (metadataParticipantsNode.isArray()) {
                for (int i = 0; i < metadataParticipantsNode.size(); i++) {
                    String puuid = metadataParticipantsNode.get(i).asText();
                    if (puuid.equals(targetPuuid)) {
                        targetIndex = i;
                        break;
                    }
                }
            }

            // info의 participants에서 사용자 정보 추출
            if (targetIndex != -1) {
                JsonNode targetParticipantNode = rootNode.path("info").path("participants").path(targetIndex);

                // targetParticipantNode에서 필요한 정보 추출
                String summonerName = targetParticipantNode.path("summonerName").asText();

                int kills = targetParticipantNode.path("kills").asInt();
                int deaths = targetParticipantNode.path("deaths").asInt();
                int assists = targetParticipantNode.path("assists").asInt();
                int championId = targetParticipantNode.path("championId").asInt();
                String championName = targetParticipantNode.path("championName").asText();

                String lane = targetParticipantNode.path("lane").asText();
                JsonNode challenges = targetParticipantNode.path("challenges");
                double kda = challenges.path("kda").asDouble();
                double killParticipation = challenges.path("killParticipation").asDouble();

                int physicalDamageDealtToChampions = targetParticipantNode.path("physicalDamageDealtToChampions").asInt();
                int magicDamageDealtToChampions = targetParticipantNode.path("magicDamageDealtToChampions").asInt();

                int stealthWardsPlaced = challenges.path("stealthWardsPlaced").asInt();
                int wardTakedowns = challenges.path("wardTakedowns").asInt();
                int controlWardsPlaced = challenges.path("controlWardsPlaced").asInt();
                int detectorWardsPlaced = targetParticipantNode.path("detectorWardsPlaced").asInt();

                int damageDealtToBuildings = targetParticipantNode.path("damageDealtToBuildings").asInt();

                int dragonTakedowns = challenges.path("dragonTakedowns").asInt();
                int baronTakedowns = challenges.path("baronTakedowns").asInt();

                int teleportTakedowns = challenges.path("teleportTakedowns").asInt();

                int totalMinionsKilled = targetParticipantNode.path("totalMinionsKilled").asInt();

                System.out.println("소환사 이름: " + summonerName);
                System.out.println("Kills: " + kills + ", Deaths: " + deaths + ", Assists: " + assists);

                return SummonerPerformance.createPlayerPerformance(targetPuuid, targetIndex, username, lane, kda, killParticipation, kills, deaths, assists, physicalDamageDealtToChampions, magicDamageDealtToChampions,
                        stealthWardsPlaced, wardTakedowns, controlWardsPlaced, detectorWardsPlaced, damageDealtToBuildings, dragonTakedowns, baronTakedowns, teleportTakedowns, totalMinionsKilled, championId, championName);

            } else {
                System.out.println("해당 puuid를 가진 소환사를 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new NoSuchElementException("해당 puuid를 가진 소환사를 찾을 수 없습니다.");
    }
}

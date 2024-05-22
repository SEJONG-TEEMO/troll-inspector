package sejong.teemo.trollinspector.util.parsing;

import co.elastic.clients.json.JsonData;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import sejong.teemo.trollinspector.domain.SummonerPerformance;
import sejong.teemo.trollinspector.record.PositionResultRecord;
import sejong.teemo.trollinspector.util.parsing.exception.SummonerException;

import java.io.IOException;

import static sejong.teemo.trollinspector.util.parsing.exception.SummonerException.ErrorType.NOT_FOUND_PUUID;

@Slf4j
public class JsonToPlayerPerformance {

    public static SummonerPerformance jsonToPlayerPerformance(String jsonInput, String username, String targetPuuid) {
        final ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode rootNode = objectMapper.readTree(jsonInput);
            int targetIndex = findTargetIndex(rootNode, targetPuuid);

            if (targetIndex != -1) {
                JsonNode targetParticipantNode = rootNode.path("info").path("participants").path(targetIndex);
                return mapToSummonerPerformance(targetParticipantNode, targetPuuid, targetIndex, username);
            } else {
                log.info("해당 PUUID를 가진 소환사를 찾을 수 없습니다.");
                throw new SummonerException(NOT_FOUND_PUUID, targetPuuid);
            }
        } catch (IOException e) {
            log.error("JSON 파싱 중 오류 발생", e);
            throw new RuntimeException("JSON 파싱 중 오류 발생", e);
        }
    }

    private static int findTargetIndex(JsonNode rootNode, String targetPuuid) {
        JsonNode metadataParticipantsNode = rootNode.path("metadata").path("participants");
        if (metadataParticipantsNode.isArray()) {
            for (int index = 0; index < metadataParticipantsNode.size(); index++) {
                String puuid = metadataParticipantsNode.get(index).asText();
                if (puuid.equals(targetPuuid)) {
                    return index;
                }
            }
        }
        return -1;
    }

    private static SummonerPerformance mapToSummonerPerformance(JsonNode node, String targetPuuid, int targetIndex, String username) {
        int kills = node.path("kills").asInt();
        int deaths = node.path("deaths").asInt();
        int assists = node.path("assists").asInt();
        int championId = node.path("championId").asInt();
        String championName = node.path("championName").asText();
        String lane = node.path("lane").asText();

        JsonNode challenges = node.path("challenges");
        double kda = challenges.path("kda").asDouble();
        double killParticipation = challenges.path("killParticipation").asDouble();

        int physicalDamageDealtToChampions = node.path("physicalDamageDealtToChampions").asInt();
        int magicDamageDealtToChampions = node.path("magicDamageDealtToChampions").asInt();
        int stealthWardsPlaced = challenges.path("stealthWardsPlaced").asInt();
        int wardTakedowns = challenges.path("wardTakedowns").asInt();
        int controlWardsPlaced = challenges.path("controlWardsPlaced").asInt();
        int detectorWardsPlaced = node.path("detectorWardsPlaced").asInt();
        int damageDealtToBuildings = node.path("damageDealtToBuildings").asInt();
        int dragonTakedowns = challenges.path("dragonTakedowns").asInt();
        int baronTakedowns = challenges.path("baronTakedowns").asInt();
        int teleportTakedowns = challenges.path("teleportTakedowns").asInt();
        int totalMinionsKilled = node.path("totalMinionsKilled").asInt();

        return SummonerPerformance.createPlayerPerformance(
                targetPuuid, targetIndex, username, lane, kda, killParticipation, kills, deaths, assists,
                physicalDamageDealtToChampions, magicDamageDealtToChampions, stealthWardsPlaced, wardTakedowns,
                controlWardsPlaced, detectorWardsPlaced, damageDealtToBuildings, dragonTakedowns, baronTakedowns,
                teleportTakedowns, totalMinionsKilled, championId, championName);
    }

    public static PositionResultRecord parseKdaScore(JsonData kdaScoreAggregate, String lane) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            PositionResultRecord positionResultRecord = mapper.readValue(kdaScoreAggregate.toJson().toString(), PositionResultRecord.class);
            return PositionResultRecord.of(lane, positionResultRecord.finalScore(), positionResultRecord.totalScore(), positionResultRecord.count());
        } catch (Exception e) {
            log.error("Error while extracting KDA Score", e);
            throw new RuntimeException("Error while extracting KDA Score", e);
        }
    }
}

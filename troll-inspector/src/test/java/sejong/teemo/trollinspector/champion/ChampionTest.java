package sejong.teemo.trollinspector.champion;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class ChampionTest {

    @Test
    void test() throws IOException {
        Map<Integer, String> championMap = getChampionMap("https://ddragon.leagueoflegends.com/cdn/14.10.1/data/en_US/champion.json");
        // 출력 테스트
        championMap.forEach((key, value) -> System.out.println("Key: " + key + ", Value: " + value));
    }

    public Map<Integer, String> getChampionMap(String url) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(new URL(url));
        JsonNode dataNode = rootNode.path("data");

        Map<Integer, String> championMap = new TreeMap<>();

        Iterator<Map.Entry<String, JsonNode>> fields = dataNode.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            JsonNode champion = field.getValue();
            int key = Integer.parseInt(champion.path("key").asText());
            String name = champion.path("name").asText();
            championMap.put(key, name);
        }

        return championMap;
    }
}

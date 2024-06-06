package sejong.teemo.trollinspector.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ChampionFinder implements InitializingBean {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    private static final Map<Long, String> championMap = new HashMap<>();
    private static final String CHAMPION_DATA_URL = "https://ddragon.leagueoflegends.com/cdn/14.10.1/data/en_US/champion.json";

    @Override
    public void afterPropertiesSet() {
        loadChampionData();
    }

    private void loadChampionData() {
        String jsonResponse = restClient.get()
                .uri(CHAMPION_DATA_URL)
                .retrieve()
                .body(String.class);

        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode dataNode = rootNode.path("data");

            dataNode.fields().forEachRemaining(field -> {
                JsonNode champion = field.getValue();
                long key = champion.path("key").asLong();
                String name = champion.path("name").asText();
                championMap.put(key, name);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String findChampionNameById(long championId) {
        return championMap.getOrDefault(championId, "Unknown Champion");
    }
}

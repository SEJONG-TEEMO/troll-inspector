package sejong.teemo.ingamesearch.champion.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import sejong.teemo.ingamesearch.champion.util.ChampionUtil;
import sejong.teemo.ingamesearch.common.generator.UriGenerator;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChampionService {

    private final RestClient restClient;

    public byte[] fetchChampionImage(Long championId) {
        String championName = ChampionUtil.mapToChampionName(championId);

        return restClient.get()
                .uri(UriGenerator.CHAMPION_IMAGE_CDN.generate(championName))
                .accept(MediaType.IMAGE_PNG)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ((request, response) -> {
                    log.info("champion image = {}", request.getURI());
                    log.error("champion image error = {}", response.getStatusText());
                    throw new IllegalArgumentException("invalid id");
                })).body(byte[].class);
    }
}

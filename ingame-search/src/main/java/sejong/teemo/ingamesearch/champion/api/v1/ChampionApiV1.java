package sejong.teemo.ingamesearch.champion.api.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sejong.teemo.ingamesearch.champion.service.ChampionService;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/champion")
public class ChampionApiV1 {

    private final ChampionService championService;

    @GetMapping("/{championId}")
    public ResponseEntity<byte[]> callApiChampionImage(@PathVariable("championId") Long championId) {

        byte[] bytes = championService.fetchChampionImage(championId);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(bytes);
    }
}

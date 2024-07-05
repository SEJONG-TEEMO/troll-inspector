package sejong.teemo.riotapi.presentation.api.v1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sejong.teemo.riotapi.application.service.UserInfoService;
import sejong.teemo.riotapi.common.dto.SummonerDto;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/teemo.gg/api/v1")
public class SummonerApiV1 {

    private final UserInfoService userInfoService;

    @GetMapping("/summoner/{encryptedSummonerId}")
    public ResponseEntity<SummonerDto> callRiotSummoner(
            @PathVariable("encryptedSummonerId") String encryptedSummonerId) {
        return ResponseEntity.ok(userInfoService.callApiSummoner(encryptedSummonerId));
    }

    @GetMapping("/summoner/by-puuid/{puuid}")
    public ResponseEntity<SummonerDto> callRiotSummonerByPuuid(@PathVariable("puuid") String puuid) {
        return ResponseEntity.ok(userInfoService.callApiSummonerByPuuid(puuid));
    }
}

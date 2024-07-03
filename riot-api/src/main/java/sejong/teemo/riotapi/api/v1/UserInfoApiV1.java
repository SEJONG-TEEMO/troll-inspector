package sejong.teemo.riotapi.api.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sejong.teemo.riotapi.dto.UserInfoDto;
import sejong.teemo.riotapi.service.UserInfoService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teemo.gg/api/v1")
public class UserInfoApiV1 {

    private final UserInfoService userInfoService;

    @GetMapping("/user-info/{division}/{tier}/{queue}")
    public ResponseEntity<List<UserInfoDto>> callApiUserInfo(@PathVariable("division") String division,
                                                             @PathVariable("tier") String tier,
                                                             @PathVariable("queue") String queue,
                                                             @RequestParam("page") int page) {

        return ResponseEntity.ok(userInfoService.callApiUserInfo(division, tier, queue, page));
    }
}

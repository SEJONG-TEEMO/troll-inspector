package sejong.teemo.riotapi.presentation.api.v1;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sejong.teemo.riotapi.application.service.UserInfoService;
import sejong.teemo.riotapi.common.dto.UserInfoDto;

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

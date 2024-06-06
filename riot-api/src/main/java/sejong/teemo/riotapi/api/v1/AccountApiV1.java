package sejong.teemo.riotapi.api.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sejong.teemo.riotapi.dto.Account;
import sejong.teemo.riotapi.facade.UserInfoFacade;
import sejong.teemo.riotapi.service.AccountService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teemo.gg/api/v1")
public class AccountApiV1 {

    private final UserInfoFacade userInfoFacade;

    @GetMapping("/account/{encryptedPuuid}")
    public ResponseEntity<Account> callApiAccount(@PathVariable String encryptedPuuid) {

        Account account = userInfoFacade.callApiAccount(encryptedPuuid);

        return ResponseEntity.ok(account);
    }
}

package sejong.teemo.riotapi.presentation.api.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sejong.teemo.riotapi.presentation.dto.Account;
import sejong.teemo.riotapi.presentation.api.external.AccountExternalApi;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teemo.gg/api/v1")
public class AccountApiV1 {

    private final AccountExternalApi accountExternalApi;

    @GetMapping("/account/{encryptedPuuid}")
    public ResponseEntity<Account> callApiAccount(@PathVariable("encryptedPuuid") String encryptedPuuid) {

        Account account = accountExternalApi.callRiotAccount(encryptedPuuid);

        return ResponseEntity.ok(account);
    }

    @GetMapping("/account/{gameName}/{tagLine}")
    public ResponseEntity<Account> callApiAccount(@PathVariable("gameName") String gameName,
                                                  @PathVariable("tagLine") String tagLine) {

        return ResponseEntity.ok(accountExternalApi.callRiotAccount(gameName, tagLine));
    }
}

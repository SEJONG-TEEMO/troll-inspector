package sejong.teemo.ingamesearch.ingame.dto.normal;

import sejong.teemo.ingamesearch.ingame.dto.user.UserInfoDto;
import sejong.teemo.ingamesearch.ingame.dto.user.UserInfoView;

public record NormalView(
        UserInfoView userInfoView,
        byte[] championImage,
        double winRate,
        double kda,
        int kills,
        int deaths,
        int assists,
        double cs,
        double averageGold,
        int pentaKill,
        int quadraKill,
        int tripleKill,
        int wins,
        int losses
) {
}

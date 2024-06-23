package sejong.teemo.ingamesearch.ingame.dto.user.performance;

import lombok.Builder;
import sejong.teemo.ingamesearch.ingame.dto.normal.NormalView;

@Builder
public record UserChampionPerformanceDto(
        byte[] championImage,
        int recentGameCount,
        double winRate,
        double kda,
        double kills,
        double deaths,
        double assists,
        double cs,
        int pentaKill,
        int quadraKill,
        int tripleKill,
        int wins,
        int losses
) {

    public static UserChampionPerformanceDto of(byte[] championImage, NormalView normalView) {
        return UserChampionPerformanceDto.builder()
                .championImage(championImage)
                .recentGameCount(normalView.recentGameCount())
                .winRate(normalView.winRate())
                .kda(normalView.kda())
                .kills(normalView.kills())
                .deaths(normalView.deaths())
                .assists(normalView.assists())
                .cs(normalView.cs())
                .pentaKill(normalView.pentaKill())
                .quadraKill(normalView.quadraKill())
                .tripleKill(normalView.tripleKill())
                .wins(normalView.wins())
                .losses(normalView.losses())
                .build();
    }
}

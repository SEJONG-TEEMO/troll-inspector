package sejong.teemo.ingamesearch.presentation.dto.user.performance;

import lombok.Builder;
import sejong.teemo.ingamesearch.presentation.dto.normal.NormalView;

@Builder
public record UserChampionPerformanceDto(
        String championName,
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

    public static UserChampionPerformanceDto of(String championName, NormalView normalView) {
        return UserChampionPerformanceDto.builder()
                .championName(championName)
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

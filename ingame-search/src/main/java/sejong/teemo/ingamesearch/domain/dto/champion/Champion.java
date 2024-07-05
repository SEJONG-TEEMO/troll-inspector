package sejong.teemo.ingamesearch.domain.dto.champion;

import lombok.Builder;

@Builder
public record Champion(
        int kills,
        int deaths,
        int assists,
        int recentGameCount,
        double kda,
        int wins,
        int losses
) {

    public static Champion of(int kills, int deaths, int assists, int recentGameCount, double kda, int wins,
                              int losses) {
        return Champion.builder()
                .kills(kills)
                .deaths(deaths)
                .assists(assists)
                .recentGameCount(recentGameCount)
                .kda(kda)
                .wins(wins)
                .losses(losses)
                .build();
    }
}

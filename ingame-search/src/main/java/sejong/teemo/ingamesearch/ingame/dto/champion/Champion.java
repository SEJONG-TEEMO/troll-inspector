package sejong.teemo.ingamesearch.ingame.dto.champion;

public record Champion(
        double kills,
        double deaths,
        double assists,
        double rating,
        int recentGameCount
) {
}

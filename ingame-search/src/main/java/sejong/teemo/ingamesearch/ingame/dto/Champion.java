package sejong.teemo.ingamesearch.ingame.dto;

public record Champion(
        double kills,
        double deaths,
        double assists,
        double rating,
        int recentGameCount
) {
}

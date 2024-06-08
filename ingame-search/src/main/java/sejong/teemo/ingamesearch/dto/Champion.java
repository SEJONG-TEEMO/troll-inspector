package sejong.teemo.ingamesearch.dto;

public record Champion(
        double kills,
        double deaths,
        double assists,
        double rating,
        int recentGameCount
) {
}

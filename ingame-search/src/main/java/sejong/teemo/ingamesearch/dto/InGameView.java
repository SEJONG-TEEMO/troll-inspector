package sejong.teemo.ingamesearch.dto;

public record InGameView(
        Long championId,
        String gameName,
        String tagLine,
        String tier,
        int kills,
        int deaths,
        int assists,
        double rating,
        int championLevel,
        int championPoint,
        int recentGameCount
) {
}

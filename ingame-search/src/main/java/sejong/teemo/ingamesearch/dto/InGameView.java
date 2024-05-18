package sejong.teemo.ingamesearch.dto;

public record InGameView(
        Long championId,
        String gameName,
        String tagLine,
        String tier,
        int championLevel,
        int championPoint,
        Champion champion
) {
}

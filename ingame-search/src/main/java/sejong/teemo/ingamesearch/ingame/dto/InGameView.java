package sejong.teemo.ingamesearch.ingame.dto;

import lombok.Builder;

@Builder
public record InGameView(
        Long championId,
        String gameName,
        String tagLine,
        String tier,
        String rank,
        long summonerLevel,
        int profileIconId,
        int championLevel,
        int championPoint,
        Champion champion
) {

    public static InGameView of(Long championId,
                                String gameName,
                                String tagLine,
                                String tier,
                                String rank,
                                long summonerLevel,
                                int profileIconId,
                                int championLevel,
                                int championPoint,
                                Champion champion) {

        return InGameView.builder()
                .championId(championId)
                .gameName(gameName)
                .tagLine(tagLine)
                .tier(tier)
                .rank(rank)
                .summonerLevel(summonerLevel)
                .profileIconId(profileIconId)
                .championLevel(championLevel)
                .championPoint(championPoint)
                .champion(champion)
                .build();
    }
}

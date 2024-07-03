package sejong.teemo.ingamesearch.ingame.dto;

import lombok.Builder;
import sejong.teemo.ingamesearch.ingame.dto.champion.Champion;

@Builder
public record InGameView(
        long championId,
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

    public static InGameView of(long championId,
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

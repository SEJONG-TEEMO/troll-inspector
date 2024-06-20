package sejong.teemo.ingamesearch.ingame.dto;

import lombok.Builder;
import sejong.teemo.ingamesearch.ingame.dto.champion.Champion;

@Builder
public record InGameView(
        byte[] championImage,
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

    public static InGameView of(byte[] championImage,
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
                .championImage(championImage)
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

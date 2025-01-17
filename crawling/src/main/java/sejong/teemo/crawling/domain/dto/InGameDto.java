package sejong.teemo.crawling.domain.dto;

import lombok.Builder;

@Builder
public record InGameDto(
        Long championId,
        String gameName,
        int level,
        String tier,
        String rateOfWin,
        String rateOfChampionWin,
        int countChampion,
        String totalKDA,
        String KDA
) {
}

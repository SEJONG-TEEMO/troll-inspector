package sejong.teemo.crawling.dto;

import lombok.Builder;

/**
 * DTO for {@link sejong.teemo.crawling.domain.Summoner}
 */
@Builder
public record SummonerDto(String name, String tag, String tier, Long leaguePoint, int level, int wins, int losses){
}
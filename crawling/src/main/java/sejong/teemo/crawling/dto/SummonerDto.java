package sejong.teemo.crawling.dto;

/**
 * DTO for {@link sejong.teemo.crawling.domain.Summoner}
 */
public record SummonerDto(String name, String tag, String tier, Long leaguePoint, int level, int wins, int losses){
}
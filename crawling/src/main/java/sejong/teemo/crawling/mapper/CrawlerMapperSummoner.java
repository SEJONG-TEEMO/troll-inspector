package sejong.teemo.crawling.mapper;

import lombok.extern.slf4j.Slf4j;
import sejong.teemo.crawling.domain.Summoner;
import sejong.teemo.crawling.dto.MatchDataDto;

import java.time.LocalDateTime;
import java.util.Arrays;

@Slf4j
public class CrawlerMapperSummoner implements CrawlerMapper<Summoner> {

    @Override
    public Summoner map(String crawlingData) {
        String[] lines = crawlingData.split("\n");
        //log.info("lines: {}", (Object) lines);

        String[] subLine = lines[3].split(" ");
        //log.info("subLine: {}", (Object) subLine);

        return isTierMapping(lines, subLine);
    }

    private static Summoner isTierMapping(String[] lines, String[] subLine) {

        Summoner.SummonerBuilder builder = Summoner.builder()
                .id(Long.parseLong(lines[0]))
                .name(lines[1])
                .tag(lines[2])
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now());

        if (subLine.length == 3) {
            builder.tier(subLine[0])
                    .leaguePoint(Long.parseLong(subLine[1].replace(",", "")))
                    .wins(extractTotalGames(lines[5]))
                    .losses(extractTotalGames(lines[6]))
                    .level(Integer.parseInt(lines[4]));

        } else if(subLine.length == 4){
            builder.tier(subLine[0] + " " + subLine[1])
                    .leaguePoint(Long.parseLong(subLine[2].replace(",", "")))
                    .wins(extractTotalGames(lines[5]))
                    .losses(extractTotalGames(lines[6]))
                    .level(Integer.parseInt(lines[4]));

        } else if(subLine.length == 5){
            builder.tier(subLine[0] + " " + subLine[1])
                    .leaguePoint(Long.parseLong(subLine[2].replace(",", "")))
                    .wins(extractTotalGames(lines[4]))
                    .losses(extractTotalGames(lines[5]))
                    .level(Integer.parseInt(subLine[4]));
        }

        return builder.build();
    }

    private static int extractTotalGames(String line) {
        // "295W" 또는 "214L"에서 승리 또는 패배 횟수 추출
        return Integer.parseInt(line.substring(0, line.length() - 1));
    }
}

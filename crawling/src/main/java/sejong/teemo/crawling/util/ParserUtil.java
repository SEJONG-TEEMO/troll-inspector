package sejong.teemo.crawling.util;

import lombok.extern.slf4j.Slf4j;
import sejong.teemo.crawling.domain.Summoner;

@Slf4j
public class ParserUtil {

    public static Summoner parseSummoner(String summonerStr) {
        String[] lines = summonerStr.split("\n");

        String[] subLine = lines[3].split(" ");

        return Summoner.builder()
                .id(Long.parseLong(lines[0]))
                .name(lines[1])
                .tag(lines[2])
                .tier(subLine[0])
                .leaguePoint(Long.parseLong(subLine[1].replace(",", "")))
                .level(Integer.parseInt(lines[4]))
                .wins(extractTotalGames(lines[5]))
                .losses(extractTotalGames(lines[6]))
                .build();
    }

    private static int extractTotalGames(String line) {
        // "295W" 또는 "214L"에서 승리 또는 패배 횟수 추출
        return Integer.parseInt(line.substring(0, line.length() - 1));
    }
}

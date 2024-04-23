package sejong.teemo.crawling.util;

import lombok.extern.slf4j.Slf4j;
import sejong.teemo.crawling.domain.Summoner;

import java.time.LocalDateTime;

@Slf4j
public class ParserUtil {

    public static Summoner parseSummoner(String summonerStr) {
        String[] lines = summonerStr.split("\n");
        //log.info("lines: {}", (Object) lines);

        String[] subLine = lines[3].split(" ");
        //log.info("subLine: {}", (Object) subLine);

        return isTierMapping(lines, subLine);
    }

    private static Summoner isTierMapping(String[] lines, String[] subLine) {
        if (subLine.length == 3) {
            return Summoner.builder()
                    .id(Long.parseLong(lines[0]))
                    .name(lines[1])
                    .tag(lines[2])
                    .tier(subLine[0])
                    .leaguePoint(Long.parseLong(subLine[1].replace(",", "")))
                    .level(Integer.parseInt(lines[4]))
                    .wins(extractTotalGames(lines[5]))
                    .losses(extractTotalGames(lines[6]))
                    .createAt(LocalDateTime.now())
                    .updateAt(LocalDateTime.now())
                    .build();
        } else if(subLine.length == 4){
            return Summoner.builder()
                    .id(Long.parseLong(lines[0]))
                    .name(lines[1])
                    .tag(lines[2])
                    .tier(subLine[0] + " " + subLine[1])
                    .leaguePoint(Long.parseLong(subLine[2].replace(",", "")))
                    .level(Integer.parseInt(lines[4]))
                    .wins(extractTotalGames(lines[5]))
                    .losses(extractTotalGames(lines[6]))
                    .createAt(LocalDateTime.now())
                    .updateAt(LocalDateTime.now())
                    .build();
        } else {
            return Summoner.builder()
                    .id(Long.parseLong(lines[0]))
                    .name(lines[1])
                    .tag(lines[2])
                    .tier(subLine[0] + " " + subLine[1])
                    .leaguePoint(Long.parseLong(subLine[2].replace(",", "")))
                    .level(Integer.parseInt(subLine[4]))
                    .wins(extractTotalGames(lines[4]))
                    .losses(extractTotalGames(lines[5]))
                    .createAt(LocalDateTime.now())
                    .updateAt(LocalDateTime.now())
                    .build();
        }
    }

    private static int extractTotalGames(String line) {
        // "295W" 또는 "214L"에서 승리 또는 패배 횟수 추출
        return Integer.parseInt(line.substring(0, line.length() - 1));
    }
}

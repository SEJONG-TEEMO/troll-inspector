package sejong.teemo.crawling.util;

import lombok.extern.slf4j.Slf4j;
import sejong.teemo.crawling.domain.Summoner;
import sejong.teemo.crawling.dto.MatchDataDto;

import java.time.LocalDateTime;
import java.util.Arrays;

@Deprecated
@Slf4j
public class ParserUtil {

    public static Summoner parseSummoner(String summonerStr) {
        String[] lines = summonerStr.split("\n");
        //log.info("lines: {}", (Object) lines);

        String[] subLine = lines[3].split(" ");
        //log.info("subLine: {}", (Object) subLine);

        return isTierMapping(lines, subLine);
    }

    public static MatchDataDto parseMatchData(String matchStr) {

        String[] lines = matchStr.split("\n");

        String[] killDeathAssist = lines[5].split("/");
        String[] ratings = lines[6].split(":");
        String[] killParticipation = lines[7].split(" ");
        String[] controlWards = lines[8].split(" ");
        String[] totalMinionsKilled = lines[9].split(" ");

        log.info("kill death assist: {}", Arrays.toString(killDeathAssist));
        log.info("rating: {}", Arrays.toString(ratings));
        log.info("killParticipation: {}", Arrays.toString(killParticipation));
        log.info("controlWards: {}", Arrays.toString(controlWards));
        log.info("totalMinionsKilled: {}", Arrays.toString(totalMinionsKilled));

        return isMappingMatchData(killDeathAssist, ratings, killParticipation, controlWards, totalMinionsKilled);
    }

    private static MatchDataDto isMappingMatchData(String[] killDeathAssist, String[] ratings, String[] killParticipation, String[] controlWards, String[] totalMinionsKilled) {

        String kills = killDeathAssist[0].trim();
        String deaths = killDeathAssist[1].trim();
        String assists = killDeathAssist[2].trim();
        String rating = ratings[0];
        String userKillParticipation = killParticipation[1].substring(0, killParticipation[1].length() - 1);
        String controlWard = controlWards[2].trim();
        String totalMinions = totalMinionsKilled[1];

        return MatchDataDto.builder()
                .kills(Integer.parseInt(kills))
                .deaths(Integer.parseInt(deaths))
                .assists(Integer.parseInt(assists))
                .rating(Double.parseDouble(rating))
                .killParticipation(Integer.parseInt(userKillParticipation))
                .controlWardsPlaced(Integer.parseInt(controlWard))
                .totalMinionKilled(Integer.parseInt(totalMinions))
                .build();
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

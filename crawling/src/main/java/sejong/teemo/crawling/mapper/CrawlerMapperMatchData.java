package sejong.teemo.crawling.mapper;

import lombok.extern.slf4j.Slf4j;
import sejong.teemo.crawling.dto.MatchDataDto;

import java.util.Arrays;

@Slf4j
public class CrawlerMapperMatchData implements CrawlerMapper<MatchDataDto> {

    @Override
    public MatchDataDto map(String crawlingData) {
        String[] lines = crawlingData.split("\n");

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

}

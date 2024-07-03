package sejong.teemo.crawling.common.mapper;

import sejong.teemo.crawling.dto.InGameDto;

public class CrawlerMapperInGame implements CrawlerMapper<InGameDto> {

    @Override
    public InGameDto map(String crawlingData) {
        String[] split = crawlingData.split("\n");

        InGameDto.InGameDtoBuilder inGameDtoBuilder = InGameDto.builder()
                .championId(0L)
                .gameName(split[0])
                .level(Integer.parseInt(split[1].split(" ")[1]))
                .tier(split[2])
                .rateOfWin(split[3]);

        if(split.length == 9) {
            inGameDtoBuilder
                    .rateOfChampionWin(split[4])
                    .countChampion(Integer.parseInt(split[5].replaceAll("[()]", "").split(" ")[0]))
                    .totalKDA(split[6])
                    .KDA(split[7]);
        } else {
            inGameDtoBuilder.KDA(split[4]);
        }

        return inGameDtoBuilder.build();
    }
}

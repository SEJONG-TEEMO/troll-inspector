package sejong.teemo.ingamesearch.presentation.dto;

import lombok.Builder;
import sejong.teemo.ingamesearch.presentation.dto.champion.ChampionMastery;

import java.util.List;

@Builder
public record SpectatorDto(Long gameId, String gameType, List<ChampionMastery> championMasteryList) {

    public static SpectatorDto of(Long gameId, String gameType, List<ChampionMastery> championMasteryList) {
        return SpectatorDto.builder()
                .gameId(gameId)
                .gameType(gameType)
                .championMasteryList(championMasteryList)
                .build();
    }
}

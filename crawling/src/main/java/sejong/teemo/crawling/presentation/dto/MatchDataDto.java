package sejong.teemo.crawling.presentation.dto;

import lombok.Builder;

@Builder
public record MatchDataDto(int kills, int deaths, int assists, double rating, int totalMinionKilled, int controlWardsPlaced, double killParticipation) {

    @Override
    public String toString() {
        return "MatchDataDto{" +
                "kills=" + kills +
                ", deaths=" + deaths +
                ", assists=" + assists +
                ", rating=" + rating +
                ", totalMinionKilled=" + totalMinionKilled +
                ", controlWardsPlaced=" + controlWardsPlaced +
                ", killParticipation=" + killParticipation +
                '}';
    }
}

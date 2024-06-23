package sejong.teemo.ingamesearch.ingame.dto.user;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;

@Builder
public record UserInfoView(
        String gameName,
        String tagLine,
        String tier,
        String rank,
        int total,
        int wins,
        int losses,
        int profileIconId) {

    @QueryProjection
    public UserInfoView(String gameName, String tagLine, String tier, String rank, int total, int wins, int losses, int profileIconId) {
        this.gameName = gameName;
        this.tagLine = tagLine;
        this.tier = tier;
        this.rank = rank;
        this.total = total;
        this.wins = wins;
        this.losses = losses;
        this.profileIconId = profileIconId;
    }
}

package sejong.teemo.ingamesearch.domain.dto.normal;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import sejong.teemo.ingamesearch.domain.dto.user.UserInfoView;

@Builder
public record NormalView(
        UserInfoView userInfoView,
        int championId,
        int recentGameCount,
        double winRate,
        double kda,
        double kills,
        double deaths,
        double assists,
        double cs,
        int pentaKill,
        int quadraKill,
        int tripleKill,
        int wins,
        int losses
) {

    @QueryProjection
    public NormalView(UserInfoView userInfoView, int championId, int recentGameCount, double winRate, double kda,
                      double kills, double deaths, double assists, double cs, int pentaKill, int quadraKill,
                      int tripleKill, int wins, int losses) {
        this.userInfoView = userInfoView;
        this.championId = championId;
        this.recentGameCount = recentGameCount;
        this.winRate = winRate;
        this.kda = kda;
        this.kills = kills;
        this.deaths = deaths;
        this.assists = assists;
        this.cs = cs;
        this.pentaKill = pentaKill;
        this.quadraKill = quadraKill;
        this.tripleKill = tripleKill;
        this.wins = wins;
        this.losses = losses;
    }
}

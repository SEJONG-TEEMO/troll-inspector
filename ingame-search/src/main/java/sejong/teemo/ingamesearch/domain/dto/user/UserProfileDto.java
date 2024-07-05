package sejong.teemo.ingamesearch.domain.dto.user;

import lombok.Builder;
import sejong.teemo.ingamesearch.domain.dto.normal.NormalView;

@Builder
public record UserProfileDto(
        long profileIconId,
        String gameName,
        String tagLine,
        String tier,
        String rank,
        int total,
        int wins,
        int losses,
        long summonerLevel
) {

    public static UserProfileDto from(NormalView normalView) {
        return UserProfileDto.builder()
                .profileIconId(normalView.userInfoView().profileIconId())
                .gameName(normalView.userInfoView().gameName())
                .tagLine(normalView.userInfoView().tagLine())
                .tier(normalView.userInfoView().tier())
                .rank(normalView.userInfoView().rank())
                .total(normalView.userInfoView().total())
                .wins(normalView.userInfoView().wins())
                .losses(normalView.userInfoView().losses())
                .summonerLevel(normalView.userInfoView().summonerLevel())
                .build();
    }
}

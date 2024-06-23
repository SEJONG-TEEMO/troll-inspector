package sejong.teemo.ingamesearch.ingame.dto.user;

import lombok.Builder;
import sejong.teemo.ingamesearch.ingame.dto.normal.NormalView;

@Builder
public record UserProfileDto(
        byte[] profileImage,
        String gameName,
        String tagLine,
        String tier,
        String rank,
        int total,
        int wins,
        int losses
) {

    public static UserProfileDto of(byte[] profileImage, NormalView normalView) {
        return UserProfileDto.builder()
                .profileImage(profileImage)
                .gameName(normalView.userInfoView().gameName())
                .tagLine(normalView.userInfoView().tagLine())
                .tier(normalView.userInfoView().tier())
                .rank(normalView.userInfoView().rank())
                .total(normalView.userInfoView().total())
                .wins(normalView.userInfoView().wins())
                .losses(normalView.userInfoView().losses())
                .build();
    }
}

package sejong.teemo.ingamesearch.ingame.dto.user;

public record UserInfoView(
        byte[] profileIconImage,
        String tier,
        String rank,
        int wins,
        int losses
) {
}

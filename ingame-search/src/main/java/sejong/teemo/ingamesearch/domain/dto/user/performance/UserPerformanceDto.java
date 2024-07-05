package sejong.teemo.ingamesearch.domain.dto.user.performance;

import lombok.Builder;
import sejong.teemo.ingamesearch.domain.dto.user.UserProfileDto;

import java.util.List;

@Builder
public record UserPerformanceDto(
        UserProfileDto userProfileDto,
        List<UserChampionPerformanceDto> userChampionPerformances
) {

    public static UserPerformanceDto of(UserProfileDto userProfileDto,
                                        List<UserChampionPerformanceDto> userChampionPerformances) {
        return UserPerformanceDto.builder()
                .userProfileDto(userProfileDto)
                .userChampionPerformances(userChampionPerformances)
                .build();
    }
}

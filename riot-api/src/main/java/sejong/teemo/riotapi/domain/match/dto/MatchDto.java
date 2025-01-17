package sejong.teemo.riotapi.domain.match.dto;

import lombok.Builder;

@Builder
public record MatchDto(
        String userName,
        String tagLine,
        MatchDataDto matchDataDto
) {
    public static MatchDto of(String userName, String tagLine, MatchDataDto matchDataDto) {
        return MatchDto.builder()
                .userName(userName)
                .tagLine(tagLine)
                .matchDataDto(matchDataDto)
                .build();
    }
}

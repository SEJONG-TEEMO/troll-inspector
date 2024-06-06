package sejong.teemo.riotapi.dto;

import lombok.Builder;

/**
 */
@Builder
public record MiniSeriesDto(int losses, String progress, int target, int wins) {
}
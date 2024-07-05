package sejong.teemo.riotapi.common.dto;

import lombok.Builder;

/**
 *
 */
@Builder
public record MiniSeriesDto(int losses, String progress, int target, int wins) {
}
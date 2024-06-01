package sejong.teemo.riotapi.dto;

import sejong.teemo.batch.entity.MiniSeries;

/**
 * DTO for {@link MiniSeries}
 */
public record MiniSeriesDto(int losses, String progress, int target, int wins) {
}
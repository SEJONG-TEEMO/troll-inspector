package sejong.teemo.batch.dto;

import sejong.teemo.batch.entity.MiniSeries;

import java.io.Serializable;

/**
 * DTO for {@link MiniSeries}
 */
public record MiniSeriesDto(int losses, String progress, int target, int wins) {
}
package sejong.teemo.trollinspector.record;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import static sejong.teemo.trollinspector.util.parsing.MathematicalParsing.roundToTwoDecimalPlaces;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PositionResultRecord(String lane, Double finalScore, Double totalScore, Long count) {

    public static PositionResultRecord of(
            String lane, Double finalScore, Double totalScore, Long count
    ) {
        return new PositionResultRecord(
                lane,
                roundToTwoDecimalPlaces(finalScore),
                roundToTwoDecimalPlaces(totalScore),
                count
        );
    }
}

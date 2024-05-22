package sejong.teemo.trollinspector.util.parsing;

public class MathematicalParsing {
    public static Double roundToTwoDecimalPlaces(Double value) {
        if (value == null) {
            return null;
        } return Math.round(value * 100) / 100.0;
    }
}

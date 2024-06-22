package models.helpers;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class UnitConverter {
    // Returns conversion of liter to mililiter rounded in 2 decimal places
    public static double literToMililiter(double number) {
        return (Math.round((number * 1000.0) * 100.0)) / 100.0;
    }

    // Returns conversion of mililiter to liter rounded in 2 decimal places
    public static double mililiterToLiter(double number) {
        return (Math.round((number / 1000.0) * 100.0)) / 100.0;
    }

    // Returns number in String formatted up to 2 decimal places; Only use to
    // display
    public static String toTwoDecimalPlaces(double number) {
        DecimalFormat decfor = new DecimalFormat("0.00");
        decfor.setRoundingMode(RoundingMode.HALF_UP);
        return decfor.format(number);
    }
}

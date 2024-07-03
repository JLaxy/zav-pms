package models.helpers;

public class StringHelper {
    public static String toTitleCase(String input) {
        StringBuilder titleCase = new StringBuilder(input.length());
        boolean nextTitleCase = true;
        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }
            titleCase.append(c);
        }
        return titleCase.toString();
    }

    public static String convertSizeToDatabaseFormat(String size) {
        if (size.endsWith("L")) {
            // Convert liters to milliliters
            double value = Double.parseDouble(size.replace("L", "").trim());
            return String.format("%.0f", value * 1000); // No decimals for whole numbers
        }
        if (size.endsWith("ml")) {
            // Directly use milliliters value
            double value = Double.parseDouble(size.replace("ml", "").trim());
            return String.format("%.0f", value); // No decimals for whole numbers
        }
        // For sizes that might be in other units (e.g., without units or other formats)
        return size.trim();
    }
}

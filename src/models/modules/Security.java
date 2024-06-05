package models.modules;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Security {
    private static final int OTP_LENGTH = 6;

    // Generates OTP needed for Login
    public static String generateOTP() {
        String OTP = "";

        // Loop until OTP is complete
        while (OTP.length() != OTP_LENGTH) {
            // Randomly choose number
            int ASCIIIndex = (int) (Math.random() * 101);
            // If chosen ASCII index is a number or a lower case or upper case letter
            if ((ASCIIIndex >= 48 && ASCIIIndex <= 57) || (ASCIIIndex >= 65 && ASCIIIndex <= 90)
                    || (ASCIIIndex >= 97 && ASCIIIndex <= 122))
                OTP += (char) ASCIIIndex;
        }

        return OTP;
    }

    // Returns name of the system where program is running
    public static String getSystemName() {
        Map<String, String> env = System.getenv();
        if (env.containsKey("COMPUTERNAME"))
            return env.get("COMPUTERNAME");
        else if (env.containsKey("HOSTNAME"))
            return env.get("HOSTNAME");
        else
            return "Unknown Computer";
    }

    // Returns error string of password; returns empty string if no errors
    public static String isNewPasswordValid(String password) {
        Pattern specialCharPatten = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Pattern UpperCasePatten = Pattern.compile("[A-Z ]");
        Pattern lowerCasePatten = Pattern.compile("[a-z ]");
        Pattern digitCasePatten = Pattern.compile("[0-9 ]");

        if (password.length() < 8 || password.length() > 22)
            return "Password must be between 8 to 22 characters!";
        if (!specialCharPatten.matcher(password).find())
            return "Password must have a special character!";
        if (!UpperCasePatten.matcher(password).find())
            return "Password must have an uppercase character!";
        if (!lowerCasePatten.matcher(password).find())
            return "Password must have a lowercase character!";
        if (!digitCasePatten.matcher(password).find())
            return "Password must have a number!";

        return "";
    }

    // Returns true if email is valid
    public static boolean isEmailValid(String email) {
        Pattern emailRegex = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher verifier = emailRegex.matcher(email);
        return verifier.matches();
    }
}

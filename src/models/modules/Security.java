package models.modules;

import java.util.Map;

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
}

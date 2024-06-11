/*
 * Class that handles date operations throughout the program
 */

package models.helpers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import enums.ProgramSettings;

public class DateHelper {

    // Easy to change values
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("u-MM-d HH:mm:ss");

    // Returns the Date Object equivalent of String
    public static LocalDateTime stringToDate(String date) {
        try {
            return LocalDateTime.parse(date, DATE_FORMATTER);
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, "models.helpers.DateHelper");
            return null;
        }
    }

    // Returns formatted string equivalent of date
    public static String dateToString(LocalDateTime date) {
        try {
            return date.format(DATE_FORMATTER);
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, "models.helpers.DateHelper");
            return null;
        }
    }

    // Returns formatted current date time
    public static String getCurrentDateTimeString() {
        return dateToString(LocalDateTime.now());
    }

    // Returns true if date supplied has already passed
    public static Boolean isDateBeforeNow(LocalDateTime date1) {
        return date1.isBefore(LocalDateTime.now());
    }

    // Returns current date time
    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }

    // Adds minutes to supplied date
    public static LocalDateTime addMinutes(LocalDateTime date, int minutes) {
        return date.plusMinutes(minutes);
    }

    // Returns the seconds between the cooldown
    public static long getLoginCooldownSecs() {
        return ChronoUnit.SECONDS.between(getCurrentDateTime(),
                DateHelper.stringToDate(new JSONManager().getSetting(ProgramSettings.Setting.COOLDOWN.getValue())));
    }

    // Returns date in formatted String
    public static String dateToFormattedDate(LocalDate date) {
        String[] info = date.toString().split("-");
        info[1] = DateHelper.intToMonth(Integer.parseInt(info[1]));
        return info[1] + " " + info[2] + ", " + info[0];
    }

    // Returns the String equivalent of the month
    public static String intToMonth(int numberMonth) {
        switch (numberMonth) {
            case 1:
                return "Jan.";
            case 2:
                return "Feb.";
            case 3:
                return "Mar.";
            case 4:
                return "Apr.";
            case 5:
                return "May";
            case 6:
                return "Jun.";
            case 7:
                return "Jul.";
            case 8:
                return "Aug.";
            case 9:
                return "Sept.";
            case 10:
                return "Oct.";
            case 11:
                return "Nov.";
            case 12:
                return "Dec.";
            default:
                return "Error";
        }
    }
}

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
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("u-MM-d");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("u-MM-d HH:mm:ss");
    private static final DateTimeFormatter READABLE_DATE_FORMATTER = DateTimeFormatter.ofPattern("MMMM d, Y");

    // Returns the Date Object equivalent of String
    public static LocalDateTime stringToDateTime(String date) {
        try {
            return LocalDateTime.parse(date, DATE_TIME_FORMATTER);
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, "models.helpers.DateHelper");
            return null;
        }
    }

    public static LocalDate stringToDate(String date) {
        try {
            return LocalDate.parse(date, DATE_FORMATTER);
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, "models.helpers.DateHelper");
            return null;
        }
    }

    // Returns formatted string equivalent of datetime
    public static String dateTimeToString(LocalDateTime date) {
        try {
            return date.format(DATE_TIME_FORMATTER);
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, "models.helpers.DateHelper");
            return null;
        }
    }

    // Returns formatted string equivalent of date
    public static String dateToString(LocalDate date) {
        try {
            return date.format(DATE_FORMATTER);
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, "models.helpers.DateHelper");
            return null;
        }
    }

    // Returns formatted current date time
    public static String getCurrentDateTimeString() {
        return dateTimeToString(LocalDateTime.now());
    }

    // Returns true if date supplied has already passed
    public static Boolean isDateTimeBeforeNow(LocalDateTime date1) {
        return date1.isBefore(LocalDateTime.now());
    }

    public static Boolean isDateBeforeNow(LocalDate date1) {
        return date1.isBefore(LocalDate.now());
    }

    public static Boolean isDateNow(LocalDate date1) {
        return date1.isEqual(LocalDate.now());
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
                DateHelper.stringToDateTime(new JSONManager().getSetting(ProgramSettings.Setting.COOLDOWN.getValue())));
    }

    // Returns date in formatted String
    public static String dateToFormattedDate(LocalDate date) {
        return date.format(READABLE_DATE_FORMATTER);
    }

}

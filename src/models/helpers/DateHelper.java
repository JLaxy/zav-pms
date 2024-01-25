/*
 * Class that handles date operations throughout the program
 */

package models.helpers;

import java.util.Calendar;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateHelper {
    // Returns the Date Object equivalent of String
    public static Date stringToDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            // Create date object from String
            Date stringDate = format.parse(date);
            return stringDate;
            // System.out.println(format.format(stringDate));
        } catch (ParseException e) {
            PopupDialog.showErrorDialog(e, "models.helpers.DateHelper");
            return null;
        }

    }

    // Returns formatted string equivalent of date
    public static String dateToString(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    // Returns formatted current date time
    public static String getCurrentDateTimeString() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    // Returns true if date supplied has already passed
    public static Boolean isDateBeforeNow(Date date1) {
        return date1.before(new Date(System.currentTimeMillis()));
    }

    // Returns current date time
    public static Date getCurrentDateTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return format.parse(getCurrentDateTimeString());
        } catch (ParseException e) {
            PopupDialog.showErrorDialog(e, "models.helpers.DateHelper");
            return null;
        }
    }

    // Adds minutes to supplied date
    public static Date addMinutes(Date date, int minutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, 5);
        cal.getTime();
        return cal.getTime();
    }
}

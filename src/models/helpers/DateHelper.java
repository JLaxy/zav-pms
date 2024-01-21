/*
 * Class that handles date operations throughout the program
 */

package models.helpers;

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
            e.printStackTrace();
            return null;
        }

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
}

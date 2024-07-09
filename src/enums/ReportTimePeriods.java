/*
 * Contains defined time periods of reports
 * 
 * For better readability
 */

package enums;

public class ReportTimePeriods {
    public enum TimePeriod {
        // TimePeriod(id)

        DAILY(1, 1, "Daily"),
        WEEKLY(2, 7, "Weekly"),
        MONTHLY(3, 30, "Monthly");

        private final int id;
        // Number of days
        private final int daysCount;
        private final String periodString;

        // Constructor for ENUM
        TimePeriod(int id, int daysCount, String periodString) {
            this.id = id;
            this.daysCount = daysCount;
            this.periodString = periodString;
        }

        // Returns the value of the time period in database
        public int getValue() {
            return this.id;
        }

        public int getDays() {
            return this.daysCount;
        }

        public String getPeriodString() {
            return this.periodString;
        }

    }
}
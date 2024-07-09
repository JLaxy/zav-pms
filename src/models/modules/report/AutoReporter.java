package models.modules.report;

import java.time.LocalDate;
import java.util.Timer;
import java.util.TimerTask;

import enums.ProgramSettings;
import enums.ReportTimePeriods;
import models.helpers.DateHelper;
import models.helpers.JSONManager;
import models.modules.Emailer;
import models.schemas.User;

public class AutoReporter {
    public static void setupAutoReporterTask() {
        Timer reportTime = new Timer(true);
        TimerTask reporter = new TimerTask() {
            @Override
            public void run() {
                System.out.println("Running auto report timer");

                // Finding time period
                ReportTimePeriods.TimePeriod period = null;
                if (new JSONManager().getSetting(ProgramSettings.Setting.REPORT_TIME_INTERVAL.getValue())
                        .compareTo("Daily") == 0)
                    period = ReportTimePeriods.TimePeriod.DAILY;
                else if (new JSONManager().getSetting(ProgramSettings.Setting.REPORT_TIME_INTERVAL.getValue())
                        .compareTo("Weekly") == 0)
                    period = ReportTimePeriods.TimePeriod.WEEKLY;
                else if (new JSONManager().getSetting(ProgramSettings.Setting.REPORT_TIME_INTERVAL.getValue())
                        .compareTo("Monthly") == 0)
                    period = ReportTimePeriods.TimePeriod.MONTHLY;

                String fileName = new ReportManager(User.getSystemUser()).createDailyPeriodicSales(period,
                        DateHelper.dateToString(LocalDate.now()));

                if (fileName == null) {
                    System.out.println("Failed to automatically generate automatic Periodic Sales Report!");
                    return;
                }

                if (Emailer.sendReport(
                        new JSONManager().getSetting(ProgramSettings.Setting.EMAIL_REPORT_DESTINATION.getValue()),
                        fileName, User.getSystemUser(), fileName)) {
                    System.out.println("Successfully sent email report to email \""
                            + new JSONManager().getSetting(ProgramSettings.Setting.EMAIL_REPORT_DESTINATION.getValue())
                            + "\"");
                }
                // Check interval
                // Generate report
                // Send to email
            }
        };

        long interval = 0;

        if (new JSONManager().getSetting(ProgramSettings.Setting.REPORT_TIME_INTERVAL.getValue())
                .compareTo("none") == 0) {
            System.out.println("no selected auto report time interval! auto reporter will not run.");
            return;
        } else if (new JSONManager().getSetting(ProgramSettings.Setting.EMAIL_REPORT_DESTINATION.getValue())
                .compareTo("none") == 0) {
            System.out.println("no selected email report destination! auto reporter will not run.");
            return;
        } else if (new JSONManager().getSetting(ProgramSettings.Setting.REPORT_TIME_INTERVAL.getValue())
                .compareTo("Daily") == 0) {
            interval = 24 * 60 * 60 * 1000;
        } else if (new JSONManager().getSetting(ProgramSettings.Setting.REPORT_TIME_INTERVAL.getValue())
                .compareTo("Weekly") == 0) {
            interval = 7L * 24 * 60 * 60 * 1000;
        } else if (new JSONManager().getSetting(ProgramSettings.Setting.REPORT_TIME_INTERVAL.getValue())
                .compareTo("Monthly") == 0) {
            interval = 30L * 24 * 60 * 60 * 1000;
        }

        reportTime.scheduleAtFixedRate(reporter, 0, interval);
        System.out.println("auto reporter scheduled to run: "
                + new JSONManager().getSetting(ProgramSettings.Setting.REPORT_TIME_INTERVAL.getValue()));
    }
}

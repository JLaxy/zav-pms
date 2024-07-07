package models.modules.report;

import java.util.Timer;
import java.util.TimerTask;

import enums.ProgramSettings;
import models.helpers.JSONManager;

public class AutoReporter {
    public static void setupAutoReporterTask() {
        Timer reportTime = new Timer(true);
        TimerTask reporter = new TimerTask() {
            @Override
            public void run() {
                System.out.println("auto report timer ran");
                System.out.println("TODO: GENERATE THEN SEND REPORT TO EMAIL");
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
                .compareTo("daily") == 0) {
            interval = 24 * 60 * 60 * 1000;
        } else if (new JSONManager().getSetting(ProgramSettings.Setting.REPORT_TIME_INTERVAL.getValue())
                .compareTo("weekly") == 0) {
            interval = 7L * 24 * 60 * 60 * 1000;
        } else if (new JSONManager().getSetting(ProgramSettings.Setting.REPORT_TIME_INTERVAL.getValue())
                .compareTo("monthly") == 0) {
            interval = 30L * 24 * 60 * 60 * 1000;
        }

        reportTime.scheduleAtFixedRate(reporter, 0, interval);
        System.out.println("auto reporter scheduled to run: "
                + new JSONManager().getSetting(ProgramSettings.Setting.REPORT_TIME_INTERVAL.getValue()));
    }
}

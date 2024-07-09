package models.report;

import java.sql.Date;
import java.time.LocalDate;

import javax.swing.JOptionPane;

import controllers.report.ManualGenerateReportController;
import enums.ReportTimePeriods;
import enums.UserLogActions;
import models.helpers.DateHelper;
import models.helpers.PopupDialog;
import models.modules.Emailer;
import models.modules.report.ReportManager;
import models.schemas.User;

public class ManualGenerateReportModel {

    private ManualGenerateReportController controller;

    public ManualGenerateReportModel(ManualGenerateReportController controller) {
        this.controller = controller;
    }

    public boolean generatePeriodicReport(ReportTimePeriods.TimePeriod timePeriod, String date, User loggedInUser) {
        String fileName = new ReportManager(loggedInUser).createDailyPeriodicSales(timePeriod, date);
        try {
            if (fileName != null) {
                // Logging Action to Database
                this.controller.getDBManager().query.logAction(loggedInUser.getId(), loggedInUser.getUname(),
                        UserLogActions.Actions.GENERATED_PERIODIC_SALES_REPORT.getValue(),
                        DateHelper.dateTimeToString(DateHelper.getCurrentDateTime()), "");
                if (PopupDialog
                        .confirmOperationDialog(
                                "Report Generated! Do you want a copy to be sent to your email?") != JOptionPane.YES_OPTION)
                    return true;
                if (Emailer.sendReport(loggedInUser.getEmail(), timePeriod.getPeriodString() + " Periodic Sales Report",
                        loggedInUser, fileName)) {

                    // Logging Action to Database
                    this.controller.getDBManager().query.logAction(loggedInUser.getId(), loggedInUser.getUname(),
                            UserLogActions.Actions.SENT_GENERATED_REPORT_TO_EMAIL.getValue(),
                            DateHelper.dateTimeToString(DateHelper.getCurrentDateTime()),
                            "sent a copy of the report to email \"" + loggedInUser.getEmail() + "\"");

                    PopupDialog.showInfoDialog("Report Sent",
                            "Report successfully sent to Email Address \"" + loggedInUser.getEmail() + "\"");
                    return true;
                }
            }
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
            e.printStackTrace();
        }
        return false;
    }
}

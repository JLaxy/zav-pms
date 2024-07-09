package models.report;

import controllers.report.ManualGenerateReportController;
import enums.ReportTimePeriods;
import models.modules.report.ReportManager;
import models.schemas.User;

public class ManualGenerateReportModel {

    private ManualGenerateReportController controller;

    public ManualGenerateReportModel(ManualGenerateReportController controller) {
        this.controller = controller;
    }

    public boolean generatePeriodicReport(ReportTimePeriods.TimePeriod timePeriod, String date, User loggedInUser) {
        return new ReportManager(loggedInUser).createDailyPeriodicSales(timePeriod, date);
    }
}

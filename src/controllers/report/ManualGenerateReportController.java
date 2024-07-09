package controllers.report;

import controllers.ParentController;
import enums.ReportTimePeriods;
import enums.ScreenPaths;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import models.helpers.PopupDialog;
import models.report.ManualGenerateReportModel;

public class ManualGenerateReportController extends ParentController {

    private ManualGenerateReportModel model;

    @FXML
    private void initialize(){
        this.model = new ManualGenerateReportModel(this);
    }

    @FXML
    private void goBack() {
        System.out.println("going back...");
        this.borderPaneRootSwitcher.goBack_BP();
    }

    @FXML
    private void periodicsales() {
        PS_DateIntervalSelectionController controller = (PS_DateIntervalSelectionController) this
                .initializePopUpDialog(ScreenPaths.Paths.DATE_INTERVAL_SELECTION.getPath(), loggedInUserInfo);
        controller.initialize(this);
    }

    public void generatePeriodicSales(ReportTimePeriods.TimePeriod timePeriod, String selectedDate) {
        Task<Void> reportGenerator = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    if (!model.generatePeriodicReport(timePeriod, selectedDate, loggedInUserInfo)) {
                        Platform.runLater(
                                () -> PopupDialog.showCustomErrorDialog("Failed to generate Periodic Sales Report!"));
                        return null;
                    }
                    Platform.runLater(() -> PopupDialog.showInfoDialog("Report Generated",
                            "Successfully Generated Periodic Sales Report!"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        reportGenerator.setOnRunning(e -> this.borderPaneRootSwitcher.showLoadingScreen_BP());
        reportGenerator.setOnSucceeded(e -> this.borderPaneRootSwitcher.exitLoadingScreen_BP());

        Thread myThread = new Thread(reportGenerator);
        myThread.setDaemon(true);
        myThread.start();
    }

    @FXML
    private void reorderqueue() {
        System.out.println("Reorder Queue");
    }

    @FXML
    private void preorderstock() {
        System.out.println("Preorder Stock");
    }
}

package controllers.report;

import controllers.ParentController;
import javafx.fxml.FXML;

public class ReportController extends ParentController {
    @FXML
    private void goBack() {
        System.out.println("going back...");
        this.borderPaneRootSwitcher.goBack_BP();
    }

    @FXML
    private void manualgeneratereport() {
        initializeNextScreen_BP("../../views/fxmls/report/ManualGenerateReportView.fxml", this.loggedInUserInfo, "REPORT");
        System.out.println("Manual Generate Report");
    }

    @FXML
    private void editautoreportgeneration() {
        initializeNextScreen_BP("../../views/fxmls/report/EditAutoReportGenerationView.fxml", this.loggedInUserInfo, "REPORT");
        System.out.println("Edit Auto Report Generation");
    }
}

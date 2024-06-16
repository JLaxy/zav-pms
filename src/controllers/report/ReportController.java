package controllers.report;

import controllers.ParentController;
import enums.ScreenPaths;
import javafx.fxml.FXML;

public class ReportController extends ParentController {
    @FXML
    private void goBack() {
        System.out.println("going back...");
        this.borderPaneRootSwitcher.goBack_BP();
    }

    @FXML
    private void manualgeneratereport() {
        initializeNextScreen_BP(ScreenPaths.Paths.MANUAL_GENERATE_REPORT.getPath(), this.loggedInUserInfo, "REPORT");
        System.out.println("Manual Generate Report");
    }

    @FXML
    private void editautoreportgeneration() {
        initializeNextScreen_BP(ScreenPaths.Paths.EDIT_AUTO_REPORT_GENERATION.getPath(), this.loggedInUserInfo,
                "REPORT");
        System.out.println("Edit Auto Report Generation");
    }
}

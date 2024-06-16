package controllers.report;

import controllers.ParentController;
import enums.ScreenPaths;
import javafx.fxml.FXML;

public class EditAutoReportGenerationController extends ParentController {
    @FXML
    private void goBack() {
        System.out.println("going back...");
        this.borderPaneRootSwitcher.goBack_BP();
    }

    @FXML
    private void eardestinationemail() {
        initializeNextScreen_BP(ScreenPaths.Paths.EDIT_AUTO_REPORT_DESTINATION_EMAIL.getPath(), this.loggedInUserInfo,
                "REPORT");
        System.out.println("Edit Auto Report Destination Email");
    }

    @FXML
    private void eartimeinterval() {
        initializeNextScreen_BP(ScreenPaths.Paths.EDIT_AUTO_REPORT_TIME_INTERVAL.getPath(), this.loggedInUserInfo,
                "REPORT");
        System.out.println("Edit Auto Report Time Interval");
    }

}

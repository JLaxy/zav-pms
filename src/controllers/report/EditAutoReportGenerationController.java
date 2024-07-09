package controllers.report;

import controllers.ParentController;
import enums.ProgramSettings;
import enums.ScreenPaths;
import javafx.fxml.FXML;
import models.helpers.JSONManager;
import models.helpers.PopupDialog;

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
        if (new JSONManager().getSetting(ProgramSettings.Setting.EMAIL_REPORT_DESTINATION.getValue())
                .compareTo("none") == 0) {
            PopupDialog.showCustomErrorDialog("Please provide an Automatic Report Email Destination first!");
            return;
        }

        initializeNextScreen_BP(ScreenPaths.Paths.EDIT_AUTO_REPORT_TIME_INTERVAL.getPath(), this.loggedInUserInfo,
                "REPORT");
        System.out.println("Edit Auto Report Time Interval");
    }

}

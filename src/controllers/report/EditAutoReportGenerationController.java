package controllers.report;

import controllers.ParentController;
import javafx.fxml.FXML;

public class EditAutoReportGenerationController extends ParentController {
    @FXML
    private void goBack() {
        System.out.println("going back...");
        this.borderPaneRootSwitcher.goBack_BP();
    }

    @FXML
    private void eardestinationemail() {
        initializeNextScreen_BP("../../views/fxmls/report/EditAutoReportDestinationEmailView.fxml", this.loggedInUserInfo, "REPORT");
        System.out.println("Edit Auto Report Destination Email");
    }

    @FXML
    private void eartimeinterval() {
        initializeNextScreen_BP("../../views/fxmls/report/EditAutoReportTimeIntervalView.fxml", this.loggedInUserInfo, "REPORT");
        System.out.println("Edit Auto Report Time Interval");
    }
    
}

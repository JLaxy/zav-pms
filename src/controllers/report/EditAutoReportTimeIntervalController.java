package controllers.report;

import controllers.ParentController;
import javafx.fxml.FXML;

public class EditAutoReportTimeIntervalController extends ParentController {
    @FXML
    private void goBack() {
        System.out.println("going back...");
        this.borderPaneRootSwitcher.goBack_BP();
    }

    @FXML
    private void update() {
        System.out.println("Update");
    }
    
}

package controllers.report;

import controllers.ParentController;
import enums.ProgramSettings;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import models.helpers.JSONManager;
import models.helpers.PopupDialog;
import models.modules.Security;

public class EditAutoReportDestinationEmailController extends ParentController {

    @FXML
    private TextField newEmailField;

    @FXML
    private void goBack() {
        System.out.println("going back...");
        this.borderPaneRootSwitcher.goBack_BP();
    }

    @FXML
    private void update() {
        if (this.newEmailField.getText().isBlank()) {
            PopupDialog.showCustomErrorDialog("Email is blank!");
            return;
        }

        if (!Security.isEmailValid(this.newEmailField.getText())) {
            PopupDialog.showCustomErrorDialog("Email is invalid!");
            return;
        }

        if (new JSONManager().writeToSetting(ProgramSettings.Setting.EMAIL_REPORT_DESTINATION.getValue(),
                this.newEmailField.getText())) {
            PopupDialog.showInfoDialog("Success", "Email Report Destination has been updated.");
            this.borderPaneRootSwitcher.goBack_BP();
        }
    }

}

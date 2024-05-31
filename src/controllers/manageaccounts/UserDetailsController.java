package controllers.manageaccounts;

import controllers.ParentController;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import models.schemas.User;

public class UserDetailsController extends ParentController {

    @FXML
    TextField unameField, passField, emailField, secAnsField;

    @FXML
    private void cancel() {
        this.borderPaneRootSwitcher.exitPopUpDialog();
    }

    @FXML
    private void update() {
        System.out.println("updating...");
    }

    @FXML
    private void deactivate() {
        System.out.println("deactivating...");
    }

    @FXML
    private void reactivate() {
        System.out.println("reactivate...");
    }

    // Configures and display current user details
    public void configureUserInfo(User selectedUser) {
        // Putting values on fields
        this.unameField.setText(selectedUser.getUname());
        this.passField.setText(selectedUser.getPass());
        this.emailField.setText(selectedUser.getEmail());
        this.secAnsField.setText(selectedUser.getUniqueQuestionAnswer());
    }
}

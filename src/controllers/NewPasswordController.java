package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import models.NewPasswordModel;
import models.helpers.PopupDialog;
import models.modules.Security;

public class NewPasswordController extends ParentController {
    @FXML
    private TextField newPassField, confirmPassField;
    @FXML
    private Button verifyButton, cancelButton;
    @FXML
    private Label newPassErrorLabel, confirmPassErrorLabel;

    private NewPasswordModel model;

    @FXML
    public void initialize() {
        newPassErrorLabel.setVisible(false);
        confirmPassErrorLabel.setVisible(false);
        this.model = new NewPasswordModel(this);

    }

    // Verify Button Action
    public void verifyAction(ActionEvent e) {
        System.out.println("verifying");
        String newPassword = newPassField.getText();
        // Retrieves password error text
        String newPasswordErrorText = Security.isNewPasswordValid(newPassword);

        // If has error
        if (newPasswordErrorText.length() > 0) {
            newPassErrorLabel.setText(newPasswordErrorText);
            newPassErrorLabel.setVisible(true);
            return;
        }

        newPassErrorLabel.setText("");

        // If passwords are not same
        if (arePasswordsNotSame()) {
            confirmPassErrorLabel.setText("Passwords do not match!");
            confirmPassErrorLabel.setVisible(true);
            return;
        }

        newPassErrorLabel.setText("");

        System.out.println("changing password to " + newPassword + "...");
        if (this.model.updatePassword(this.loggedInUserInfo.get("uname"), newPassword)) {
            PopupDialog.showInfoDialog("Updated Password",
                    "Successfully updated password of user \"" + this.loggedInUserInfo.get("uname") + "\"");
            // Return to login view
            rootSwitcher.goBack(3);
        }
    }

    // Returns true if new password and confirm password are same
    private boolean arePasswordsNotSame() {
        return newPassField.getText().compareTo(confirmPassField.getText()) != 0;
    }
}

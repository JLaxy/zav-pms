package controllers.manageaccounts;

import controllers.ParentController;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import models.helpers.PopupDialog;
import models.manageaccounts.UserDetailsModel;
import models.schemas.User;

public class UserDetailsController extends ParentController {

    @FXML
    private TextField unameField, passField, emailField, secAnsField;

    @FXML
    private ComboBox<String> accountTypeCBox, secQuesCBox;

    private UserDetailsModel model;
    private User selectedUser;
    private ManageAccountsController manageAccountsController;

    @FXML
    public void initialize() {
        this.model = new UserDetailsModel(this);
    }

    @FXML
    private void cancel() {
        // Exit popup dialog
        this.borderPaneRootSwitcher.exitPopUpDialog();
    }

    @FXML
    private void update() {
        System.out.println("updating...");
    }

    @FXML
    private void deactivate() {
        // If success
        if (this.model.deactivate(this.selectedUser, this.loggedInUserInfo)) {
            // Exit pop-up dialog
            this.borderPaneRootSwitcher.exitPopUpDialog();
            // Show dialog
            PopupDialog.showInfoDialog("Deactivated Account",
                    "Successfully deactivated user account \"" + this.selectedUser.getUname() +
                            "\"");
            // Update Table View
            this.manageAccountsController.syncUserTableView();
        }
    }

    // Reactivate user account
    @FXML
    private void reactivate() {

        // If success
        if (this.model.reactivate(this.selectedUser, this.loggedInUserInfo)) {
            // Exit pop-up dialog
            this.borderPaneRootSwitcher.exitPopUpDialog();
            // Show dialog
            PopupDialog.showInfoDialog("Activated Account",
                    "Successfully activated user account \"" + this.selectedUser.getUname() +
                            "\"");
            // Update Table View
            this.manageAccountsController.syncUserTableView();
        }
    }

    // Configures and display current user details
    public void configureUserInfo(User selectedUser, ManageAccountsController manageAccountsController) {
        // Syncing references
        this.selectedUser = selectedUser;
        this.manageAccountsController = manageAccountsController;

        // Initialize combo box
        this.secQuesCBox.setItems(this.model.getSecurityQuestions());
        // Pre-select binded to user account
        this.secQuesCBox.getSelectionModel().select(this.model.getSecurityQuestion(selectedUser.getUname()));

        // Initialize combo box
        this.accountTypeCBox.setItems(this.model.getLevelOfAccess());
        // Pre-select binded to user account
        this.accountTypeCBox.getSelectionModel().select(selectedUser.getLevel_of_access_id_string());

        // Putting values on fields
        this.unameField.setText(selectedUser.getUname());
        this.passField.setText(selectedUser.getPass());
        this.emailField.setText(selectedUser.getEmail());
        this.secAnsField.setText(selectedUser.getUniqueQuestionAnswer());
    }
}

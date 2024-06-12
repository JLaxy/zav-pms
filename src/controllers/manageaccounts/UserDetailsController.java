package controllers.manageaccounts;

import javax.swing.JOptionPane;

import controllers.ParentController;
import enums.AccountStatuses;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import models.helpers.PopupDialog;
import models.manageaccounts.UserDetailsModel;
import models.modules.Security;
import models.schemas.User;

public class UserDetailsController extends ParentController {

    @FXML
    private TextField unameField, passField, emailField, fnameField, lnameField, secAnsField;

    @FXML
    private ComboBox<String> accountTypeCBox, secQuesCBox;

    @FXML
    private Button reactivateButton, deactivateButton;

    private UserDetailsModel model;
    private User selectedUser;
    private ManageAccountsController manageAccountsController;

    @FXML
    public void initialize(User selectedUser, ManageAccountsController manageAccountsController) {
        this.model = new UserDetailsModel(this);
        this.manageAccountsController = manageAccountsController;

        // If selected user is not null; if not saving new user
        if (selectedUser != null)
            this.configureUserInfo(selectedUser);
        else {
            initializeComboBoxes(null);
            // Hide reactivate and deactivate buttons
            this.reactivateButton.setVisible(false);
            this.deactivateButton.setVisible(false);
        }
    }

    @FXML
    private void cancel() {
        // Exit popup dialog
        this.borderPaneRootSwitcher.exitPopUpDialog();
    }

    @FXML
    private void save() {
        // If saving new user
        if (this.selectedUser == null) {
            saveNewUser();
            return;
        }

        // Show confirmation dialog
        if (PopupDialog.confirmOperationDialog("Do you want to save changes?") != JOptionPane.YES_OPTION)
            return;

        // If user input(s) are not valid
        if (!areInputsValid())
            return;

        // Retrieve values
        String loa = accountTypeCBox.getSelectionModel().getSelectedItem();
        int secQuesId = secQuesCBox.getSelectionModel().getSelectedIndex() + 1;

        User updatedUserInfo = new User(selectedUser.getId(), unameField.getText(), passField.getText(),
                emailField.getText(), (loa.compareTo("Admin") == 0 ? 1 : (loa.compareTo("Kitchen Staff") == 0 ? 2 : 3)),
                fnameField.getText(), lnameField.getText(), selectedUser.getAccount_status_id(), secQuesId,
                secAnsField.getText());

        this.borderPaneRootSwitcher.exitPopUpDialog();
        // If failed to update
        if (!this.model.updateUserDetails(selectedUser, updatedUserInfo, loggedInUserInfo)) {
            PopupDialog.showCustomErrorDialog("Failed to update user details!");
            return;
        }
        // Else
        PopupDialog.showInfoDialog("Updated User Info",
                "Successfully updated user info of user \"" + selectedUser.getUname() + "\"");
        this.manageAccountsController.syncUserTableView();

        // If user edited info of himself, force logout
        if (updatedUserInfo.getId() == loggedInUserInfo.getId())
            this.borderPaneRootSwitcher.getPageNavigatorViewController().forceLogout();

    }

    // Returns true if all user inputs are valid
    private boolean areInputsValid() {
        // If there is an empty textfield
        if (areFieldsEmpty()) {
            PopupDialog.showCustomErrorDialog("All fields must be filled!");
            return false;
        }

        // Check if password is valid
        String errorMessage = Security.isNewPasswordValid(passField.getText());
        // If there is error in password
        if (errorMessage.length() > 0) {
            PopupDialog.showCustomErrorDialog(errorMessage);
            return false;
        }

        // If email is not valid
        if (!Security.isEmailValid(emailField.getText())) {
            PopupDialog.showCustomErrorDialog("Invalid email address!");
            return false;
        }

        return true;
    }

    // Will be called when user is registering a new user
    private void saveNewUser() {

        // Show confirmation dialog
        if (PopupDialog.confirmOperationDialog("Do you want to save this user?") != JOptionPane.YES_OPTION)
            return;

        // Checks if username is not taken
        if (this.model.doesUserExist(unameField.getText())) {
            PopupDialog.showCustomErrorDialog("Username is already taken!");
            return;
        }

        // If inputs are not valid
        if (!areInputsValid())
            return;

        System.out.println("saving new user...");

        // Retrieve values
        String loa = accountTypeCBox.getSelectionModel().getSelectedItem();
        int secQuesId = secQuesCBox.getSelectionModel().getSelectedIndex() + 1;

        User updatedUserInfo = new User(0, unameField.getText(), passField.getText(),
                emailField.getText(), (loa.compareTo("Admin") == 0 ? 1 : (loa.compareTo("Kitchen Staff") == 0 ? 2 : 3)),
                fnameField.getText(), lnameField.getText(), AccountStatuses.Status.ACTIVE.getValue(), secQuesId,
                secAnsField.getText());

        this.borderPaneRootSwitcher.exitPopUpDialog();

        // If failed to save new user
        if (!this.model.saveNewUser(updatedUserInfo, this.loggedInUserInfo)) {
            PopupDialog.showCustomErrorDialog("Failed to save new user!");
            return;
        }

        // Else
        PopupDialog.showInfoDialog("Saved New User",
                "Successfully saved new user \"" + updatedUserInfo.getUname() + "\"");
        this.manageAccountsController.syncUserTableView();
    }

    // Returns true if fields are not empty
    private boolean areFieldsEmpty() {
        return unameField.getText().isBlank() || passField.getText().isBlank() || emailField.getText().isBlank()
                || secAnsField.getText().isBlank();
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

    // Fills Combo boxes
    private void initializeComboBoxes(User selectedUser) {
        ObservableList<String> secQuesList = this.model.getSecurityQuestions();

        // Initialize combo boxes
        this.secQuesCBox.setItems(secQuesList);
        this.accountTypeCBox.setItems(this.model.getLevelOfAccess());

        // If adding new user, select first option
        if (selectedUser == null) {
            this.accountTypeCBox.getSelectionModel().selectFirst();
            this.secQuesCBox.getSelectionModel().selectFirst();
        } else {
            // Pre-select binded to user account
            this.accountTypeCBox.getSelectionModel().select(selectedUser.getLevel_of_access_id_string());
            // Pre-select binded to user account
            this.secQuesCBox.getSelectionModel()
                    .select(secQuesList.indexOf(this.model.getSecurityQuestion(selectedUser.getUname())));
        }

    }

    // Configures and display current user details
    private void configureUserInfo(User selectedUser) {
        // Syncing references
        this.selectedUser = selectedUser;

        // Initalize comboxes
        initializeComboBoxes(selectedUser);

        // Putting values on fields
        this.unameField.setText(selectedUser.getUname());
        this.passField.setText(selectedUser.getPass());
        this.emailField.setText(selectedUser.getEmail());
        this.fnameField.setText(selectedUser.getFName());
        this.lnameField.setText(selectedUser.getLName());
        this.secAnsField.setText(selectedUser.getUniqueQuestionAnswer());
    }
}

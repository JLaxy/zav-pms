package controllers;

import java.util.Map;
import java.util.Timer;

import enums.AccountStatuses;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import models.LoginModel;
import models.helpers.LoginCooldownTimerTask;
import models.helpers.PopupDialog;

public class LoginController extends ParentController {

    @FXML
    private TextField unameField, passField;
    @FXML
    private Button loginButton;
    @FXML
    private Label errorLabel;

    private final String INVALID_COMBINATION_MSG = "Invalid username and password combination!";
    private LoginModel model;
    private LoginCooldownTimerTask cooldownTimerTask;
    private Timer cooldownTimer = new Timer(true);

    @FXML
    public void initialize() {
        // Creating Model
        this.model = new LoginModel(this);
        // Checking for cooldown
        checkCooldown();
    }

    public Label getErrorLabel() {
        return this.errorLabel;
    }

    // Login Button Action
    public void loginAction(ActionEvent e) {
        try {
            // Retrieving user information from model
            Map<String, String> userInfo = this.model.getUserInfo(unameField.getText(), passField.getText());
            // If user exists
            if (userInfo.get("id") != "") {
                // If user account is disabled
                if (userInfo.get("account_status_id")
                        .compareTo(String.valueOf(AccountStatuses.Status.DISABLED.getValue())) == 0) {
                    PopupDialog.showCustomErrorDialog(
                            "Your account is currently disabled. Please contact your administrator!");
                    return;
                }
                System.out.println(
                        "exists: " + userInfo.get("id") + ", " + userInfo.get("uname") + ", "
                                + userInfo.get("account_status_id") + ", " + userInfo.get("level_of_access_id"));
                // Hiding error label
                errorLabel.setVisible(false);
                this.model.resetAttempts();
                // Clearing fields
                this.unameField.clear();
                this.passField.clear();
                // Initiating OTP Process and Passing User ID
                OTPLoginController nextController = (OTPLoginController) initializeNextScreen(
                        "../views/fxmls/OTPLoginView.fxml", Integer.parseInt(userInfo.get("id")));
                nextController.initialize(userInfo.get("email"), userInfo.get("level_of_access_id"));
            } else {
                System.out.println("does not exists");
                // Updating login attempt
                this.model.updateLoginAttempt();
                checkCooldown();
                // Showing error label
                errorLabel.setVisible(true);
            }

        } catch (Exception ex) {
            PopupDialog.showErrorDialog(ex, this.getClass().getName());
        }
    }

    // Forgot Password Action
    public void forgotPasswordAction(ActionEvent e) {
        // Navigate to Forgot Password Screen
        initializeNextScreen("../views/fxmls/ForgotPasswordView.fxml", loggedInUser);
        System.out.println("running");
    }

    // Checks if cooldown is active
    public void checkCooldown() {
        // If cooldown is active; added hasNoAttempts() method because there seems to be
        // a delay when writing JSON files; system is reading outdated version of
        // program settings
        if (this.model.isCooldownActive() || this.model.hasNoAttempts()) {
            // Disabling fields and button
            allowControlsTo(false);
            cooldownTimerTask = new LoginCooldownTimerTask(this);
            cooldownTimer.schedule(cooldownTimerTask, 0, 1000);
            return;
        }
        // else, Configuring Error Label
        allowControlsTo(true);
        errorLabel.setText(INVALID_COMBINATION_MSG);
        errorLabel.setVisible(false);
    }

    // Enables Fields and Buttons if set to true
    private void allowControlsTo(Boolean bool) {
        unameField.setDisable(!bool);
        passField.setDisable(!bool);
        loginButton.setDisable(!bool);
    }

}
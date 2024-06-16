package controllers.login;

import java.util.Timer;

import controllers.PageNavigatorViewController;
import controllers.ParentController;
import enums.AccountStatuses;
import enums.ScreenPaths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import models.login.LoginModel;
import models.schemas.User;
import models.helpers.JSONManager;
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

    // Returns Error Label
    public Label getErrorLabel() {
        return this.errorLabel;
    }

    // Login Button Action
    public void loginAction(ActionEvent e) {
        try {
            // Retrieving user information from model
            User userInfo = this.model.getUserInfo(unameField.getText(), passField.getText());
            // If user exists
            if (userInfo.getId() != 0) {
                // If user account is disabled
                if (userInfo.getAccount_status_id() == AccountStatuses.Status.DISABLED.getValue()) {
                    PopupDialog.showCustomErrorDialog(
                            "Your account is currently disabled. Please contact your administrator!");
                    return;
                }
                System.out.println(
                        "exists: " + userInfo.getId() + ", " + userInfo.getUname() + ", "
                                + userInfo.getAccount_status_id() + ", " + userInfo.getLevel_of_access_id());
                // Hiding error label
                errorLabel.setVisible(false);
                this.model.resetAttempts();
                // Clearing fields
                this.unameField.clear();
                this.passField.clear();

                // If Skip OTP is enabled
                if (new JSONManager().getDeveloperSetting("skipOTP").compareTo("true") == 0) {
                    PopupDialog.showInfoDialog("Developer Setting", "Skipping OTP");
                    PageNavigatorViewController controller = (PageNavigatorViewController) initializeNextScreen(
                            ScreenPaths.Paths.PAGE_NAVIGATOR.getPath(), userInfo);
                    // Sync screen with passed user details
                    controller.configureScreen();
                    return;
                }

                // Initiating OTP Process and Passing User ID
                OTPLoginController nextController = (OTPLoginController) initializeNextScreen(
                        ScreenPaths.Paths.OTP_LOGIN.getPath(), userInfo);
                nextController.initialize(userInfo.getEmail());
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
        initializeNextScreen(ScreenPaths.Paths.FORGOT_PASSWORD.getPath(), loggedInUserInfo);
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
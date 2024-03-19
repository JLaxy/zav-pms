package controllers;

import java.util.Timer;

import javax.swing.JOptionPane;

import enums.UserLogActions;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.TextAlignment;
import models.OTPLoginModel;
import models.helpers.DateHelper;
import models.helpers.OTPExpiryTimerTask;
import models.helpers.PopupDialog;
import models.modules.Emailer;
import models.modules.Security;

public class OTPLoginController extends ParentController {

    private final String EXPIRED_OTP = "The OTP has expired! Resend the OTP.";
    private final String WRONG_OTP = "Incorrect OTP! Please try again.";

    private String email;
    private OTPLoginModel model;
    private OTPExpiryTimerTask otpExpiryTimerTask;
    private Timer expiryTimer = new Timer(true);

    // Retrieving FXML View Elements
    @FXML
    private TextField otpField;
    @FXML
    private Button verifyButton;
    @FXML
    private Label errorLabel;

    // Immediately sets up controller and send OTP
    @FXML
    public void initialize(String email) {
        errorLabel.setVisible(false);
        this.model = new OTPLoginModel(this);
        // Send OTP to user attempting to login
        setUserEmail(email);
        sendOTP();
    }

    // Button Click; Checks if OTP matches
    public void pressed() {
        if (this.model.isCorrectOTP(otpField.getText()))
            System.out.println("correct");
        else {
            // Updating attempts
            this.model.updateOTPAttempt();
            // Updating error label
            this.errorLabel.setText(WRONG_OTP);
            this.errorLabel.setVisible(true);
            // If no more attempts, then lock UI
            if (this.model.hasNoAttempts()) {
                // Disabling Controls
                allowControlsTo(false);
                // Logging OTP Fail
                this.model.logOTPAuthentication(this.loggedInUser, UserLogActions.Actions.FAILED_OTP);
                // Showing Pop-Up dialog
                PopupDialog.showCustomErrorDialog("You have failed the OTP Authentication! You will be logged out.");
                // Cancelling Timer
                this.expiryTimer.cancel();
                this.rootSwitcher.goBack();
            }
        }
    }

    // Cancels OTP Transaction
    public void cancelAction() {
        // Confirmation Diaglog
        if (PopupDialog.cancelOperationDialog() == JOptionPane.YES_OPTION) {
            this.model.logOTPAuthentication(this.loggedInUser, UserLogActions.Actions.CANCELLED_OTP);
            this.rootSwitcher.goBack();
        }
    }

    // Assigning User Email
    private void setUserEmail(String email) {
        this.email = email;
    }

    // Sends System Generated OTP
    public void sendOTP() {
        // Generating OTP
        String OTP = Security.generateOTP();
        // Setting OTP to model
        this.model.setCurrentOTP(OTP);
        System.out.println("Sending OTP \"" + OTP + "\" to " + email);

        // Checks if OTP has been successfully sent
        if (Emailer.sendOTP(email, OTP))
            System.out.println("OTP Successfully Sent!");
        else
            System.out.println("Error");

        // Starting new expiry timer
        otpExpiryTimerTask = new OTPExpiryTimerTask(this, DateHelper.addMinutes(DateHelper.getCurrentDateTime(), 1));
        expiryTimer.schedule(otpExpiryTimerTask, 0, 1000);
    }

    // Enables Fields and Buttons if set to true
    private void allowControlsTo(Boolean bool) {
        otpField.setDisable(!bool);
        verifyButton.setDisable(!bool);
    }

    // Shows error message and disables controls since OTP has expired
    public void otpHasExpired() {
        // Showing Error Text
        this.errorLabel.setText(EXPIRED_OTP);
        this.errorLabel.setVisible(true);
        // Disabling Controls
        this.allowControlsTo(false);
    }
}

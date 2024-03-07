package controllers;

import javax.swing.JOptionPane;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import models.OTPLoginModel;
import models.helpers.PopupDialog;
import models.modules.Emailer;
import models.modules.Security;

public class OTPLoginController extends ParentController {

    private String email;
    private OTPLoginModel model;

    // Retrieving FXML View Elements
    @FXML
    private TextField otpField;
    @FXML
    private Button verifyButton;

    // Immediately sets up controller and send OTP
    @FXML
    public void initialize(String email) {
        this.model = new OTPLoginModel(this);
        // Send OTP to user attempting to login
        setUserEmail(email);
        sendOTP();
    }

    public void pressed() {
        allowControlsTo(false);
    }

    // Cancels OTP Transaction
    public void cancelAction() {
        // Confirmation Diaglog
        if (PopupDialog.cancelOperationDialog() == JOptionPane.YES_OPTION) {
            this.model.cancelledOTPAuthentication(this.loggedInUser);
            this.rootSwitcher.goBack();
        }
    }

    // Assigning User Email
    private void setUserEmail(String email) {
        System.out.println("setted");
        this.email = email;
    }

    // Sends System Generated OTP
    private void sendOTP() {
        String OTP = Security.generateOTP();
        System.out.println("Sending OTP \"" + OTP + "\" to " + email);

        if (Emailer.sendOTP(email, OTP))
            System.out.println("OTP Successfully Sent!");
        else
            System.out.println("Error");

        // allowControlsTo(false);
    }

    private void allowControlsTo(Boolean bool) {
        otpField.setDisable(!bool);
        verifyButton.setDisable(!bool);
    }
}

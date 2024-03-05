package controllers;

import javax.swing.JOptionPane;

import javafx.fxml.FXML;
import models.helpers.PopupDialog;
import models.modules.Emailer;
import models.modules.Security;

public class OTPLoginController extends ParentController {

    private String email;

    @FXML
    public void initialize(String email) {
        // Send OTP to user attempting to login
        setUserEmail(email);
        sendOTP();
    }

    public void pressed() {
        System.out.println("button pressed");
        sendOTP();
    }

    // Cancels OTP Transaction
    public void cancelAction() {
        if (PopupDialog.cancelOperationDialog() == JOptionPane.YES_OPTION)
            this.rootSwitcher.goBack();
    }

    // Assigning User Email
    private void setUserEmail(String email) {
        System.out.println("setted");
        this.email = email;
    }

    private void sendOTP() {
        String OTP = Security.generateOTP();
        System.out.println("Sending OTP \"" + OTP + "\" to " + email);

        if (Emailer.sendOTP(email, OTP))
            System.out.println("OTP Successfully Sent!");
        else
            System.out.println("Error");
    }
}

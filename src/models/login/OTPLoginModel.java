package models.login;

import controllers.login.OTPLoginController;
import enums.UserLogActions;
import models.helpers.DateHelper;

public class OTPLoginModel {
    private OTPLoginController controller;
    private int attempts;
    private String currentOTP;

    public OTPLoginModel(OTPLoginController controller) {
        this.controller = controller;
        this.attempts = 3;
        System.out.println("initiated");
    }

    // Logging OTP Actions (Cancelled or Failed)
    public void logOTPAuthentication(int user_id, UserLogActions.Actions action) {
        // Retrieving username
        String username = this.controller.getDBManager().query.getUname(user_id);
        // Logging to Database
        this.controller.getDBManager().query.logAction(user_id, username,
                action.getValue(), DateHelper.getCurrentDateTimeString(), "");
    }

    // Sets current OTP being tracked
    public void setCurrentOTP(String currentOTP) {
        this.currentOTP = currentOTP;
    }

    // Returns True if user supplied OTP matches generated OTP
    public boolean isCorrectOTP(String userOTP) {
        return userOTP.equals(this.currentOTP);
    }

    // Called if user did not match OTP; updating OTP attempts
    public void updateOTPAttempt() {
        --this.attempts;
        System.out.println("Attempts left: " + this.attempts);
    }

    // Returns True if user has no more attempts
    public Boolean hasNoAttempts() {
        if (this.attempts < 1) {
            System.out.println("no more attempts, logging out...");
            return true;
        }
        return false;
    }

}

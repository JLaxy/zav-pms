package models;

import controllers.OTPLoginController;
import enums.UserLogActions;
import models.helpers.DateHelper;

public class OTPLoginModel {
    private OTPLoginController controller;

    public OTPLoginModel(OTPLoginController controller) {
        this.controller = controller;
    }

    // Logging cancellation of OTP Authentication
    public void cancelledOTPAuthentication(int user_id) {
        // Retrieving username
        String username = this.controller.getDBManager().query.getUname(user_id);
        // Logging to Database
        this.controller.getDBManager().query.logOTPAuthentication(user_id, username,
                UserLogActions.Actions.CANCELLED_OTP.getValue(), DateHelper.getCurrentDateTimeString());
    }

}

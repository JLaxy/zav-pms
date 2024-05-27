package models.login;

import java.util.Map;

import controllers.login.ForgotPasswordController;
import enums.UserLogActions;
import models.helpers.DateHelper;

public class ForgotPasswordModel {
    ForgotPasswordController controller;

    public ForgotPasswordModel(ForgotPasswordController controller) {
        this.controller = controller;
    }

    // Returns true if username exists
    public Map<String, String> getUserInfo(String uname) {
        return this.controller.getDBManager().query.getUserInfo(uname);
    }

    // Returns details of users
    public String[] getUserQuestions(String uname) {
        return this.controller.getDBManager().query.getSecurityQuestions(uname);
    }

    // Logs Forgot Password initiation on Database
    public void logPasswordReset(String id, String uName) {
        this.controller.getDBManager().query.logAction(Integer.valueOf(id), uName,
                UserLogActions.Actions.INITIATED_PASSWORD_RESET.getValue(), DateHelper.getCurrentDateTimeString());
    }
}

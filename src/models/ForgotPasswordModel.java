package models;

import controllers.ForgotPasswordController;

public class ForgotPasswordModel {
    ForgotPasswordController controller;

    public ForgotPasswordModel(ForgotPasswordController controller) {
        this.controller = controller;
    }

    // Returns true if username exists
    public boolean doesUsernameExist(String uname) {
        return this.controller.getDBManager().query.doesUserNameExist(uname);
    }

    // Returns details of users
    public String[] getUserQuestions(String uname) {
        return this.controller.getDBManager().query.getSecurityQuestions(uname);
    }
}

package models.login;

import controllers.login.PasswordQuestionController;
import enums.UserLogActions;
import models.helpers.DateHelper;

public class PasswordQuestionModel {
    private PasswordQuestionController controller;

    public PasswordQuestionModel(PasswordQuestionController controller) {
        this.controller = controller;
    }

    // Logging Action to Database
    public void logAction(String id, String uName) {
        this.controller.getDBManager().query.logAction(Integer.valueOf(id), uName,
                UserLogActions.Actions.CANCELLED_PASSWORD_RESET.getValue(), DateHelper.getCurrentDateTimeString());
    }
}

package models;

import java.time.LocalDateTime;

import controllers.LoginController;
import models.helpers.DateHelper;
import models.helpers.JSONManager;
import models.helpers.PopupDialog;

public class LoginModel extends ParentModel {

    LoginController controller;
    private int attempts;
    // Number of attempts += 1
    private int ATTEMPT_COUNT = 2;

    public LoginModel(LoginController controller) {
        this.controller = controller;
        resetAttempts();
    }

    public void resetAttempts() {
        this.attempts = ATTEMPT_COUNT;
        System.out.println("attempts resetted!");
    }

    // Returns true if cooldown is active
    public Boolean isCooldownActive() {
        // Retrieving Cooldown Date from settings file
        LocalDateTime cooldownDate = DateHelper.stringToDate(new JSONManager().getLoginCooldown());
        // If cooldown date has not elapsed
        if (!DateHelper.isDateBeforeNow(cooldownDate))
            return true;
        return false;
    }

    // Returning user information (level of access, userID)
    public String[] getUserInfo(String uname, String pass) {
        try {
            return this.controller.getDBManager().query.userLogin(uname, pass);

        } catch (Exception ex) {
            PopupDialog.showErrorDialog(ex, this.getClass().getName());
            return null;
        }
    }

    // Updating attempt count
    public void updateLoginAttempt() {
        if (attempts < 1) {
            // add cooldown
            updateLoginCooldown();
        }
        --this.attempts;
        System.out.println("attempts left: " + this.attempts);
    }

    // Adding 5 minutes to cooldown, updating to settings
    private void updateLoginCooldown() {
        JSONManager jsonManager = new JSONManager();
        jsonManager.updateLoginCooldown();
    }

    public Boolean hasNoAttempts() {
        if (this.attempts < 0) {
            resetAttempts();
            return true;
        }
        return false;
    }
}

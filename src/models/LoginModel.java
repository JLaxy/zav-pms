package models;

import java.util.Date;

import controllers.LoginController;
import models.helpers.DateHelper;
import models.helpers.JSONManager;

public class LoginModel extends ParentModel {

    LoginController controller;

    public LoginModel(LoginController controller) {
        this.controller = controller;
    }

    // Returns true if cooldown is active
    public Boolean isCooldownActive() {
        // Retrieving Cooldown Date from settings file
        JSONManager myJSONManger = new JSONManager();
        Date cooldownDate = DateHelper.stringToDate(myJSONManger.getLoginCooldown());
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
            System.out.println("Error at: " + getClass());
            ex.printStackTrace();
            return null;
        }
    }
}

package models;

import controllers.NewPasswordController;

public class NewPasswordModel {
    private NewPasswordController controller;

    public NewPasswordModel(NewPasswordController controller) {
        this.controller = controller;
    }

    // Updates new password in database
    public boolean updatePassword(String username, String newPassword) {
        return this.controller.getDBManager().query.updateUserPassword(username, newPassword);
    }
}

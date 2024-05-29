package models.manageaccounts;

import controllers.manageaccounts.ManageAccountsController;
import javafx.collections.ObservableList;
import models.schemas.User;

public class ManageAccountsModel {

    private ManageAccountsController controller;

    public ManageAccountsModel(ManageAccountsController controller) {
        this.controller = controller;
    }

    // Returns all of the users registered in database in ObservableList
    public ObservableList<User> getAllUsers() {
        return this.controller.getDBManager().query.getAllUsers();
    }

    // Converts all user passwords into asterisk
    public ObservableList<User> mutePasswords(ObservableList<User> myList) {
        for (User user : myList) {
            user.setPass("*".repeat(user.getPass().length()));
        }
        return myList;
    }

}

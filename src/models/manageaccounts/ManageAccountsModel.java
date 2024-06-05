package models.manageaccounts;

import controllers.manageaccounts.ManageAccountsController;
import enums.UserLogActions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.helpers.DateHelper;
import models.schemas.User;

public class ManageAccountsModel {

    private ManageAccountsController controller;

    public ManageAccountsModel(ManageAccountsController controller) {
        this.controller = controller;
    }

    // Returns all of the users registered in database in ObservableList
    public ObservableList<User> getAllUsers() {
        return this.controller.getDBManager().query.getAllUsers(null);
    }

    // Returns all users matching username
    public ObservableList<User> getAllUsers(String userQuery) {
        return this.controller.getDBManager().query.getAllUsers(userQuery);
    }

    // Converts all user passwords into asterisk
    public ObservableList<User> mutePasswords(ObservableList<User> myList) {
        for (User user : myList) {
            user.setPass("*".repeat(user.getPass().length()));
        }
        return myList;
    }

    // Returns a copy of userlist
    public ObservableList<User> copyUserList(ObservableList<User> origList) {
        ObservableList<User> newList = FXCollections.observableArrayList();
        // Iterate through each user and get a copy
        for (User user : origList) {
            newList.add(user.getCopy());
        }
        return newList;
    }

    // Logs viewing of details of user to database
    public void logViewingUserDetails(int id, String uname, String viewedAccountUname) {
        this.controller.getDBManager().query.logAction(id, uname,
                UserLogActions.Actions.VIEWED_ACCOUNT_DETAILS.getValue(), DateHelper.getCurrentDateTimeString(),
                "viewed account details of \"" + viewedAccountUname + "\"");
    }

}

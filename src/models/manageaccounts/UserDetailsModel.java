package models.manageaccounts;

import controllers.manageaccounts.UserDetailsController;
import enums.AccountStatuses;
import enums.DatabaseLists;
import javafx.collections.ObservableList;
import models.helpers.PopupDialog;
import models.schemas.User;

public class UserDetailsModel {

    UserDetailsController controller;

    public UserDetailsModel(UserDetailsController controller) {
        this.controller = controller;
    }

    // Returns list of security questions in database
    public ObservableList<String> getSecurityQuestions() {
        return this.controller.getDBManager().query
                .getListOnDatabase(DatabaseLists.Lists.SECURITY_QUESTIONS);
    }

    // Returns list of levels of access questions in database
    public ObservableList<String> getLevelOfAccess() {
        return this.controller.getDBManager().query.getListOnDatabase(DatabaseLists.Lists.LEVELS_OF_ACCESS);
    }

    // Returns security question binded to user account
    public String getSecurityQuestion(String uname) {
        return this.controller.getDBManager().query.getSecurityQuestions(uname)[1];
    }

    // Reactivate current user
    public boolean reactivate(User selectedUser, User loggedInUser) {
        // If already active
        if (this.controller.getDBManager().query.getUserInfo(selectedUser.getUname())
                .getAccount_status_id() == AccountStatuses.Status.ACTIVE.getValue()) {
            PopupDialog.showCustomErrorDialog("User account is already active!");
            return false;
        }

        User updatedUser = selectedUser.getCopy();
        updatedUser.toggleAccountStatus();

        // Save changes to database
        return this.controller.getDBManager().query.updateUserInfo(selectedUser, updatedUser, loggedInUser,
                User.getAccountChangesMessage(selectedUser, updatedUser));
    }

    // Reactivate current user
    public boolean deactivate(User selectedUser, User loggedInUser) {
        // If already disabled
        if (this.controller.getDBManager().query.getUserInfo(selectedUser.getUname())
                .getAccount_status_id() == AccountStatuses.Status.DISABLED.getValue()) {
            PopupDialog.showCustomErrorDialog("User account is already disabled!");
            return false;
        }

        User updatedUser = selectedUser.getCopy();
        updatedUser.toggleAccountStatus();

        // Save changes to database
        return this.controller.getDBManager().query.updateUserInfo(selectedUser, updatedUser, loggedInUser,
                User.getAccountChangesMessage(selectedUser, updatedUser));
    }

    // Save updated user info
    public boolean updateUserDetails(User oldUserInfo, User newUserInfo, User loggedInUser) {
        return this.controller.getDBManager().query.updateUserInfo(oldUserInfo, newUserInfo, loggedInUser,
                User.getAccountChangesMessage(oldUserInfo, newUserInfo));
    }

    // Save new user to database
    public boolean saveNewUser(User newUser, User loggedInUser) {
        return this.controller.getDBManager().query.saveNewUser(newUser, loggedInUser);
    }

    // Returns true if user exists
    public boolean doesUserExist(String uName) {
        User retrieved = this.controller.getDBManager().query.getUserInfo(uName);

        if (retrieved.getUname() == null)
            return false;
        return true;
    }

    // Returns true if email already exists
    public boolean doesEmailExist(String email) {
        return this.controller.getDBManager().query.doesEmailExist(email);
    }

}

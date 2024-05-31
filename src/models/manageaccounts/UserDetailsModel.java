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
        // Toggling account status
        selectedUser.toggleAccountStatus();

        // Save changes to database
        return this.controller.getDBManager().query.updateUserInfo(selectedUser.getUname(), selectedUser, loggedInUser);
    }

}

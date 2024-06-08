package models.manageaccounts;

import controllers.manageaccounts.ViewUserLogsController;
import javafx.collections.ObservableList;
import models.schemas.User;
import models.schemas.UserLog;

public class ViewUserLogsModel {
    private ViewUserLogsController controller;

    public ViewUserLogsModel(ViewUserLogsController controller) {
        this.controller = controller;
    }

    // Returns list of user logs
    public ObservableList<UserLog> getUserLogs(String uname, String dateSelected) {
        return this.controller.getDBManager().query.getUserLogs(uname, dateSelected);
    }

    public User getUser(String uname) {
        return this.controller.getDBManager().query.getUserInfo(uname);
    }
}

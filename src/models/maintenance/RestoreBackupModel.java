package models.maintenance;

import controllers.maintenance.RestoreBackupController;
import enums.UserLogActions;
import javafx.collections.ObservableList;
import models.helpers.DateHelper;
import models.schemas.DatabaseLog;
import models.schemas.User;

public class RestoreBackupModel {
    RestoreBackupController controller;

    public RestoreBackupModel(RestoreBackupController controller) {
        this.controller = controller;
    }

    // Returns database logs in observable list
    public ObservableList<DatabaseLog> getDatabaseLogs() {
        return this.controller.getDBManager().query.getDatabaseLogs();
    }

    // Log database restoration
    public boolean logDatabaseRestoration(User userInfo, String saveFileName) {
        this.controller.getDBManager().query.logAction(userInfo.getId(), userInfo.getUname(),
                UserLogActions.Actions.RESTORED_DATABASE.getValue(), DateHelper.getCurrentDateTimeString(),
                "restored to save file name \"" + saveFileName + "\"");
        this.controller.getDBManager().query.logDatabaseAction(userInfo, DateHelper.getCurrentDateTimeString(),
                UserLogActions.Actions.RESTORED_DATABASE.getValue());

        return true;
    }
}

package models.maintenance;

import controllers.ParentController;
import controllers.maintenance.ManualBackupController;
import enums.UserLogActions;
import javafx.collections.ObservableList;
import models.helpers.DateHelper;
import models.helpers.JSONManager;
import models.schemas.DatabaseLog;
import models.schemas.User;

public class ManualBackupModel extends ParentController {

    private ManualBackupController controller;

    public ManualBackupModel(ManualBackupController controller) {
        this.controller = controller;
    }

    // Log database backup
    public void logBackup(User userInfo, String FILE_NAME) {
        this.controller.getDBManager().query.logAction(userInfo.getId(), userInfo.getUname(),
                UserLogActions.Actions.MANUAL_DATABASE_BACKUP.getValue(), DateHelper.getCurrentDateTimeString(),
                "saved as \"" + FILE_NAME + "\"; at location \"" + new JSONManager().getSetting("backupLocation")
                        + "\"");
        this.controller.getDBManager().query.logDatabaseAction(userInfo, DateHelper.getCurrentDateTimeString(),
                UserLogActions.Actions.MANUAL_DATABASE_BACKUP.getValue());
    }

    // Returns database logs in observable list
    public ObservableList<DatabaseLog> getDatabaseLogs() {
        return this.controller.getDBManager().query.getDatabaseLogs();
    }
}

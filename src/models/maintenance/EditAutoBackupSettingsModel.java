package models.maintenance;

import controllers.maintenance.EditAutoBackupSettingsController;
import enums.UserLogActions;
import javafx.collections.ObservableList;
import models.helpers.DateHelper;
import models.schemas.DatabaseLog;
import models.schemas.User;

public class EditAutoBackupSettingsModel {
    private EditAutoBackupSettingsController controller;

    public EditAutoBackupSettingsModel(EditAutoBackupSettingsController controller) {
        this.controller = controller;
    }

    public ObservableList<DatabaseLog> getBackupLogs() {
        return controller.getDBManager().query.getDatabaseLogs();
    }

    public void logToggle(User userInfo, UserLogActions.Actions action) {
        this.controller.getDBManager().query.logAction(userInfo.getId(), userInfo.getUname(), action.getValue(),
                DateHelper.getCurrentDateTimeString(), "");
    }
}

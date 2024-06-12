package models.maintenance;

import controllers.maintenance.EditBackupLocationController;
import enums.UserLogActions;
import models.helpers.DateHelper;
import models.schemas.User;

public class EditBackupLocationModel {
    EditBackupLocationController controller;

    public EditBackupLocationModel(EditBackupLocationController controller) {
        this.controller = controller;
    }

    // Log user action to database
    public void logBackupLocationEdit(User loggedInUser, String path) {
        this.controller.getDBManager().query.logAction(loggedInUser.getId(), loggedInUser.getUname(),
                UserLogActions.Actions.EDITED_DATABASE_BACKUP_LOCATION.getValue(),
                DateHelper.getCurrentDateTimeString(), "to path \"" + path + "\"");
    }
}

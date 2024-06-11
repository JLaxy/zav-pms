package controllers.maintenance;

import controllers.ParentController;
import javafx.fxml.FXML;
import models.helpers.JSONManager;
import models.helpers.PopupDialog;

public class MaintenanceController extends ParentController {
    @FXML
    private void goBack() {
        System.out.println("going back...");
        this.borderPaneRootSwitcher.goBack_BP();
    }

    @FXML
    private void manualbackup() {
        System.out.println("Manual Backup");

        if (new JSONManager().getSetting("backupLocation").compareTo("Nothing set.") == 0) {
            PopupDialog.showCustomErrorDialog("You have not set a backup location!");
            return;
        }

        ManualBackupController controller = (ManualBackupController) this.initializeNextScreen_BP(
                "../../views/fxmls/maintenance/ManualBackupView.fxml", loggedInUserInfo,
                "MAINTENANCE");
        controller.retrieveDatabaseLogs();
    }

    @FXML
    private void restoredatabase() {
        System.out.println("Restore Database");
    }

    @FXML
    private void editbackuplocation() {
        System.out.println("Edit Backup Location");
        initializeNextScreen_BP("../../views/fxmls/maintenance/EditBackupLocationView.fxml", loggedInUserInfo,
                "EDIT BACKUP LOCATION");
    }

    @FXML
    private void eabackupsettings() {
        System.out.println("Edit Auto Backup Settings");
    }
}

package controllers.maintenance;

import controllers.ParentController;
import javafx.fxml.FXML;

public class MaintenanceController extends ParentController {
    @FXML
    private void goBack() {
        System.out.println("going back...");
        this.borderPaneRootSwitcher.goBack_BP();
    }

    @FXML
    private void manualbackup() {
        System.out.println("Manual Backup");
    }

    @FXML
    private void restoredatabase() {
        System.out.println("Restore Database");
    }

    @FXML
    private void editbackuplocation() {
        System.out.println("Edit Backup Location");
    }

    @FXML
    private void eabackupsettings() {
        System.out.println("Edit Auto Backup Settings");
    }
}

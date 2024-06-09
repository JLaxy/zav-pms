package controllers.maintenance;

import controllers.ParentController;
import enums.ProgramSettings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import models.helpers.JSONManager;

public class ManualBackupController extends ParentController {

    @FXML
    private TableView<String> backupHistoryTable;

    @FXML
    private TableColumn<String, String> timeCol, dateCol;

    @FXML
    private Label pathLabel;

    @FXML
    public void initialize() {
        // Set settings path on screen
        this.pathLabel.setText(new JSONManager().getSetting(ProgramSettings.Setting.BACKUP_LOCATION.getValue()));
        configureTable();
    }

    private void configureTable() {
        // Disable table reordering
        backupHistoryTable.getColumns().forEach(e -> {
            e.setReorderable(false);
        });
    }

    @FXML
    private void updateLocation(ActionEvent e) {
        System.out.println("Updating location...");
    }

    @FXML
    private void goBack(ActionEvent e) {
        this.borderPaneRootSwitcher.goBack_BP();
    }

    @FXML
    private void backup(ActionEvent e) {
        System.out.println("backing up...");
    }

}

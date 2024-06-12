package controllers.maintenance;

import java.time.LocalDateTime;

import controllers.ParentController;
import enums.UserLogActions;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import models.helpers.DateHelper;
import models.helpers.JSONManager;
import models.maintenance.EditAutoBackupSettingsModel;
import models.schemas.DatabaseLog;

public class EditAutoBackupSettingsController extends ParentController {

    @FXML
    private Label onLabel, offLabel, latestLabel, previousLabel, pathLabel;

    @FXML
    private Rectangle sliderRect;

    private boolean isOn;
    private EditAutoBackupSettingsModel model;

    @FXML
    public void initialize() {
        this.model = new EditAutoBackupSettingsModel(this);
        this.isOn = Boolean.valueOf(new JSONManager().getSetting("autoBackup"));
        this.pathLabel.setText(new JSONManager().getSetting("backupLocation"));
        syncElements();
    }

    @FXML
    private void toggle(ActionEvent e) {

        // If true, set to false
        if (isOn) {
            new JSONManager().writeToSetting("autoBackup", "false");
            this.model.logToggle(loggedInUserInfo, UserLogActions.Actions.DISABLED_DATABASE_AUTOBACKUP);
        } else {
            new JSONManager().writeToSetting("autoBackup", "true");
            this.model.logToggle(loggedInUserInfo, UserLogActions.Actions.ENABLED_DATABASE_AUTOBACKUP);
        }

        this.isOn = !this.isOn;
        syncElements();
    }

    @FXML
    private void goBack(ActionEvent e) {
        this.borderPaneRootSwitcher.goBack_BP();
    }

    public void configureScreen() {
        ObservableList<DatabaseLog> dbLogs = this.model.getBackupLogs();

        if (dbLogs.isEmpty())
            return;

        LocalDateTime latestDate = DateHelper.stringToDate(dbLogs.getLast().getDateTime());
        latestLabel.setText(
                DateHelper.dateToFormattedDate(latestDate.toLocalDate()) + " "
                        + latestDate.toLocalTime());

        LocalDateTime previousDate = DateHelper
                .stringToDate(dbLogs.get(dbLogs.indexOf(dbLogs.getLast()) - 1).getDateTime());
        previousLabel.setText(DateHelper.dateToFormattedDate(previousDate.toLocalDate()) + " "
                + previousDate.toLocalTime());
    }

    // Sync element to state
    private void syncElements() {
        if (isOn) {
            onLabel.setTextFill(Color.BLACK);
            offLabel.setTextFill(Color.WHITE);
            sliderRect.setLayoutX(428);
            sliderRect.setLayoutY(252);
            return;
        }

        onLabel.setTextFill(Color.WHITE);
        offLabel.setTextFill(Color.BLACK);
        sliderRect.setLayoutX(334);
        sliderRect.setLayoutY(252);
    }
}

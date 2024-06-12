package controllers.maintenance;

import java.io.File;
import java.time.LocalDateTime;

import controllers.ParentController;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import models.helpers.DateHelper;
import models.helpers.JSONManager;
import models.helpers.PopupDialog;
import models.maintenance.RestoreBackupModel;
import models.schemas.DatabaseLog;

public class RestoreBackupController extends ParentController {

    @FXML
    private Label selectedLocLabel, currentLocLabel, latestLabel, previousLabel;

    private RestoreBackupModel model;

    @FXML
    public void initialize() {
        this.model = new RestoreBackupModel(this);
    }

    // Update label
    public void updateLabels() {
        currentLocLabel.setText(new JSONManager().getSetting("backupLocation"));
        retrieveDatabaseLogs();
    }

    // Retrieve user logs and show on UI
    public void retrieveDatabaseLogs() {
        ObservableList<DatabaseLog> dbLogs = model.getDatabaseLogs();

        LocalDateTime latestDate = DateHelper.stringToDate(dbLogs.getLast().getDateTime());
        latestLabel.setText(
                DateHelper.dateToFormattedDate(latestDate.toLocalDate()) + " " + latestDate.toLocalTime());

        LocalDateTime previousDate = DateHelper
                .stringToDate(dbLogs.get(dbLogs.indexOf(dbLogs.getLast()) - 1).getDateTime());
        previousLabel.setText(DateHelper.dateToFormattedDate(previousDate.toLocalDate()) + " "
                + previousDate.toLocalTime());

    }

    @FXML
    private void restore(ActionEvent e) {
        System.out.println("restoring...");

        // Checks if file is valid
        if (!new File(selectedLocLabel.getText()).exists()) {
            PopupDialog.showCustomErrorDialog("There is a problem with the selected directory. \nPlease try again");
            return;
        }

        Service<Void> databaseRestorer = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        // Restore to database; if not sucess
                        if (!getDBManager().restoreDatabase(selectedLocLabel.getText())) {
                            PopupDialog.showCustomErrorDialog("There is an error restoring the database!");
                        }
                        return null;
                    }
                };
            }
        };

        databaseRestorer.setOnSucceeded(ex -> {
            PopupDialog.showInfoDialog("Success", "Sucessfully restored database!");
            this.borderPaneRootSwitcher.exitLoadingScreen_BP();
            this.borderPaneRootSwitcher.goBack_BP();
            this.model.logDatabaseRestoration(loggedInUserInfo, selectedLocLabel.getText());
        });

        borderPaneRootSwitcher.showLoadingScreen_BP();
        databaseRestorer.start();
    }

    @FXML
    private void goBack(ActionEvent e) {
        System.out.println("going back...");
        this.borderPaneRootSwitcher.goBack_BP();
    }

    // Allows user to select file
    @FXML
    private void selectLocation(ActionEvent e) {
        System.out.println("selecting...");
        FileChooser chooser = new FileChooser();

        // Set initial directory to backup location set
        chooser.setInitialDirectory(new File(new JSONManager().getSetting("backupLocation")));
        File backupFile = chooser.showOpenDialog(
                this.borderPaneRootSwitcher.getPageNavigatorViewController().getRootSwitcher().getMainStage());

        if (backupFile == null)
            return;

        this.selectedLocLabel.setText(backupFile.getAbsolutePath());
    }
}

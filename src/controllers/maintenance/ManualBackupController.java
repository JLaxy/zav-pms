package controllers.maintenance;

import java.time.LocalDateTime;

import controllers.ParentController;
import enums.ProgramSettings;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.helpers.DateHelper;
import models.helpers.JSONManager;
import models.helpers.PopupDialog;
import models.maintenance.ManualBackupModel;
import models.schemas.DatabaseLog;

public class ManualBackupController extends ParentController {

    @FXML
    private TableView<DatabaseLog> backupHistoryTable;

    @FXML
    private TableColumn<DatabaseLog, String> timeCol, dateCol;

    @FXML
    private Label pathLabel, latestBackupLabel, previousBackupLabel;

    private ManualBackupModel model;

    @FXML
    public void initialize() {
        this.model = new ManualBackupModel(this);

        // Set settings path on screen
        this.pathLabel.setText(new JSONManager().getSetting(ProgramSettings.Setting.BACKUP_LOCATION.getValue()));
        configureTable();
    }

    private void configureTable() {
        // Disable table reordering
        backupHistoryTable.getColumns().forEach(e -> {
            e.setReorderable(false);
        });

        timeCol.setCellValueFactory(new PropertyValueFactory<DatabaseLog, String>("timeString"));
        dateCol.setCellValueFactory(new PropertyValueFactory<DatabaseLog, String>("dateString"));
    }

    // Retrieve user logs and show on UI
    public void retrieveDatabaseLogs() {
        try {
            ObservableList<DatabaseLog> dbLogs = model.getDatabaseLogs();

            Service<Void> datebaseLogsRetriever = new Service<Void>() {
                @Override
                protected Task<Void> createTask() {
                    return new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    backupHistoryTable.setItems(dbLogs);
                                }

                            });
                            return null;
                        }
                    };
                }
            };

            datebaseLogsRetriever.setOnSucceeded(e -> {
                borderPaneRootSwitcher.exitLoadingScreen_BP();
                try {
                    LocalDateTime latestDate = DateHelper.stringToDate(dbLogs.getLast().getDateTime());
                    latestBackupLabel.setText(
                            DateHelper.dateToFormattedDate(latestDate.toLocalDate()) + " "
                                    + latestDate.toLocalTime());

                    LocalDateTime previousDate = DateHelper
                            .stringToDate(dbLogs.get(dbLogs.indexOf(dbLogs.getLast()) - 1).getDateTime());
                    previousBackupLabel.setText(DateHelper.dateToFormattedDate(previousDate.toLocalDate()) + " "
                            + previousDate.toLocalTime());

                    // Sort according to date
                    backupHistoryTable.getSortOrder()
                            .add(backupHistoryTable.getColumns()
                                    .get(backupHistoryTable.getColumns().indexOf(dateCol)));
                } catch (Exception ex) {
                    System.out.println("no backup date / previous backup date");
                }
            });

            borderPaneRootSwitcher.showLoadingScreen_BP();
            datebaseLogsRetriever.start();
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
        }
    }

    @FXML
    private void goBack(ActionEvent e) {
        this.borderPaneRootSwitcher.goBack_BP();
    }

    @FXML
    private void backup(ActionEvent e) {
        System.out.println("backing up...");

        // If sucess
        if (!this.getDBManager().backupDatabase()) {
            PopupDialog.showCustomErrorDialog("There has been an error backing up the database");
            return;
        }

        // Log backup action to database
        this.model.logBackup(loggedInUserInfo);
        retrieveDatabaseLogs();
        PopupDialog.showInfoDialog("Success", "Sucessfully backed-up database");
    }
}

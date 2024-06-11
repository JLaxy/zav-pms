package controllers.maintenance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;

import controllers.ParentController;
import enums.ProgramSettings;
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
                            backupHistoryTable.setItems(dbLogs);
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
                            DateHelper.dateToFormattedDate(latestDate.toLocalDate()) + " " + latestDate.toLocalTime());

                    LocalDateTime previousDate = DateHelper
                            .stringToDate(dbLogs.get(dbLogs.indexOf(dbLogs.getLast()) - 1).getDateTime());
                    previousBackupLabel.setText(DateHelper.dateToFormattedDate(previousDate.toLocalDate()) + " "
                            + previousDate.toLocalTime());
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
        try {
            String[] COMMAND = new String[] {
                    "C:\\Program Files\\MySQL\\MySQL Server 8.0\\bin\\mysqldump", "-u",
                    "pmsprogram",
                    "-pzavpms@123", "zav-pms-db",
                    "-r",
                    (new JSONManager().getSetting("backupLocation") + "\\"
                            + DateHelper.getCurrentDateTimeString().replace(":", "-") + ".sql")
            };

            Runtime runner = Runtime.getRuntime();
            // Executing command
            Process proc = runner.exec(COMMAND);

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

            // Read the output from the command
            System.out.println("Here is the standard output of the command:\n");
            String s = null;
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }

            // Read any errors from the attempted command
            System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }

            PopupDialog.showInfoDialog("Success", "Sucessfully backed-up database");
            // Log backup action to database
            this.model.logBackup(loggedInUserInfo);
            retrieveDatabaseLogs();
        } catch (IOException e1) {
            PopupDialog.showErrorDialog(e1, "models.helpers.Debugger.java");
        }
    }
}

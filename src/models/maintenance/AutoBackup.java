package models.maintenance;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import enums.UserLogActions;
import javafx.stage.Stage;
import models.helpers.DateHelper;
import models.helpers.JSONManager;
import models.helpers.PopupDialog;
import models.helpers.database.DBManager;
import models.schemas.User;

public class AutoBackup {
    // Sets function to execute as program closes
    public static void enableDatabaseSaveOnExit(Stage mainStage, DBManager zavPMSDB) {

        mainStage.setOnCloseRequest(e -> {
            // If autobackup is disabled
            if (new JSONManager().getSetting("autoBackup").compareTo("false") == 0)
                return;

            try {

                // final String FILE_NAME = DateHelper.getCurrentDateTimeString().replace(":",
                // "-") + ".sql";
                final String FILE_NAME = "data.sql";

                // String[] COMMAND = new String[] {
                // "C:\\Program Files\\MySQL\\MySQL Server 8.0\\bin\\mysqldump", "-u",
                // "pmsprogram",
                // "-pzavpms@123", "zav-pms-db",
                // "-r",
                // (new JSONManager().getSetting("backupLocation") + "\\"
                // + DateHelper.getCurrentDateTimeString().replace(":", "-") + ".sql")
                // };

                // JUNELL
                String[] COMMAND = new String[] {
                        "C:\\Program Files\\MySQL\\MySQL Server 8.0\\bin\\mysqldump", "-u",
                        "pmsprogram",
                        "-pzavpms@123", "zav-pms-db",
                        "-r",
                        "F:\\Jownjown\\Education\\3rd Year\\Summer\\Software Engineering 2\\Final Project\\zav-pms\\databaseexport\\"
                                + FILE_NAME };

                // // CLARK
                // String[] COMMAND = new String[] {
                // "C:\\Program Files\\MySQL\\MySQL Server 8.0\\bin\\mysqldump", "-u",
                // "pmsprogram",
                // "-pzavpms@123", "zav-pms-db",
                // "-r",
                // "C:\\Users\\Clark\\Desktop\\zavs\\zav-pms\\databaseexport\\data.sql" };

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

                PopupDialog.showInfoDialog("Auto Database Backup", "Successfully backed up database");

                // Log automatic database backup
                zavPMSDB.query.logAction(User.getSystemUser().getId(), User.getSystemUser().getUname(),
                        UserLogActions.Actions.AUTO_DATABASE_BACKUP.getValue(), DateHelper.getCurrentDateTimeString(),
                        "saved as \"" + FILE_NAME + "\"; at location \""
                                + new JSONManager().getSetting("backupLocation") + "\"");

                // Log automatic database backup
                zavPMSDB.query.logDatabaseAction(User.getSystemUser(), DateHelper.getCurrentDateTimeString(),
                        UserLogActions.Actions.AUTO_DATABASE_BACKUP.getValue());
            } catch (Exception x) {
                PopupDialog.showErrorDialog(x, "models.helpers.AutoBackup.java");
            }
        });
    }
}

/*
 * Responsible for managing connections of application to Database
 */

package models.helpers.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import javax.swing.JOptionPane;

import models.helpers.JSONManager;
import models.helpers.PopupDialog;

public class DBManager {

    // Configuration
    private final String SERVER_ADDRESS = "127.0.0.1";
    private final String PORT_ADDRESS = "3306";
    private final String DATABASE_NAME = "zav-pms-db";
    private final String USERNAME = "pmsprogram";
    private final String PASSWORD = "zavpms@123";

    public DBQuery query;

    public DBManager() {
        loadDriver();
        // Creating DBQuery object to execute queries
        this.query = new DBQuery(this);
        // Testing connection to database
        testConnection();
    }

    // Loading Driver for Java to MySQL
    private void loadDriver() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Cannot load JDBC Driver!\n\n" + e, "Database Driver Error",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    // Used at program start to determine if application is able to connect to the
    // database
    private void testConnection() {
        if (createConnection() == null)
            System.exit(1);
    }

    // Creating connection to Database
    public Connection createConnection() {
        System.out.println("connecting...");
        try {
            return DriverManager.getConnection(
                    "jdbc:mysql://" + SERVER_ADDRESS + ":" + PORT_ADDRESS + "/" + DATABASE_NAME, USERNAME, PASSWORD);
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
            return null;
        }
    }

    // Initializing Statement Object; allows program to send SQL queries to MySQL
    public Statement createQuery(Connection con) {
        try {
            return con.createStatement();
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
            return null;
        }
    }

    public boolean restoreDatabase(String path) {
        try {
            String[] cmdString = new String[] { "mysql", DATABASE_NAME, "-u" + USERNAME, "-p" + PASSWORD, "-e",
                    " source " + path };
            Runtime runner = Runtime.getRuntime();
            Process proc = runner.exec(cmdString);

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

            // If there is an error
            if ((s = stdError.readLine()) == null)
                return true;
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
        }
        return false;
    }

    // Backups database
    public boolean backupDatabase(String FILE_NAME) {
        try {
            String[] COMMAND = new String[] {
                    "C:\\Program Files\\MySQL\\MySQL Server 8.0\\bin\\mysqldump", "-u",
                    "pmsprogram",
                    "-pzavpms@123", "zav-pms-db",
                    "-r",
                    (new JSONManager().getSetting("backupLocation") + "\\"
                            + FILE_NAME)
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

            // If no error
            if ((s = stdError.readLine()) == null)
                return true;
        } catch (IOException e1) {
            PopupDialog.showErrorDialog(e1, "models.helpers.Debugger.java");
        }
        return false;
    }
}

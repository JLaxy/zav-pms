/*
 * Responsible for managing connections of application to Database
 */

package models.helpers.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import javax.swing.JOptionPane;

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
}

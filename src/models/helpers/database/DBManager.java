/*
 * Responsible for managing connections of application to Database
 */

package models.helpers.database;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.swing.JOptionPane;

public class DBManager {

    // Configuration
    private String SERVER_ADDRESS = "127.0.0.1";
    private String PORT_ADDRESS = "3306";
    private String DATABASE_NAME = "zav-pms-db";
    private String USERNAME = "pmsprogram";
    private String PASSWORD = "zavpms@123";

    private Connection con;
    public DBQuery query;

    public DBManager() {
        loadDriver();
        createConnection();
        // Creating Database Query Class that is responsible for executing queries to
        // database
        this.query = new DBQuery(this.con);
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

    // Creating connection to Database
    private void createConnection() {
        try {
            this.con = DriverManager.getConnection(
                    "jdbc:mysql://" + SERVER_ADDRESS + ":" + PORT_ADDRESS + "/" + DATABASE_NAME, USERNAME, PASSWORD);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Unable to connect to Database!\n\n" + e,
                    "Database Connection Error",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
}

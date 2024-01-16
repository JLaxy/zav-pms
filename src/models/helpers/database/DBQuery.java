/*
 * Handles all queries executed on Database
 */

package models.helpers.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class DBQuery {
    private Connection con;
    private Statement query;

    DBQuery(Connection con) {
        this.con = con;
        createQuery();
    }

    // Initializing Statement Object; allows program to send SQL queries to MySQL
    private void createQuery() {
        try {
            this.query = this.con.createStatement();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to initialize Statement Object!\n\n" + e,
                    "Statement Initialization Error",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    // Returns TRUE if result of query has no data / rows
    public static boolean isNoResult(ResultSet result) {
        try {
            if (!result.isBeforeFirst())
                return true;
            return false;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "There was an error in DBQuery, isNoResult()!\n\n" + e,
                    "DBQuery Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        return true;
    }

    // Returns NULL instance of ResultSet if Username and Password combination does
    // not point to a specific registered account in the Database
    public ResultSet userLogin(String uname, String pass) {
        ResultSet result = null;
        try {
            result = this.query.executeQuery(
                    "SELECT uname FROM `zav-pms-db`.users WHERE uname = \"" + uname + "\" AND pass = \"" + pass
                            + "\";");
            return result;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "There was an error in executing the query in DBQuery, userLogin()!\n\n" + e,
                    "Query Execution Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        return result;
    }

}

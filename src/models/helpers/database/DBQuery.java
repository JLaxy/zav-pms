/*
 * Handles all queries executed on Database;
 * 
 * IMPORTANT: always use try-with-resources statement to immediately close the connection
 */

package models.helpers.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JOptionPane;

public class DBQuery {

    private DBManager zavPMSDB;

    DBQuery(DBManager zavPMSDB) {
        this.zavPMSDB = zavPMSDB;
    }

    // Returns TRUE if result of query has no data / rows; automatically closes
    // result set
    private static boolean isNoResult(ResultSet result) {
        try {
            if (!result.isBeforeFirst()) {
                result.close();
                return true;
            }
            result.close();
            return false;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "There was an error in DBQuery, isNoResult()!\n\n" + e,
                    "DBQuery Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        return true;
    }

    // True if the user and password combination exists in database
    public boolean userLogin(String uname, String pass) {
        // Try-with-resources; immediately closes resources before any catach or finally
        // block is executed
        try (Connection con = this.zavPMSDB.createConnection();
                PreparedStatement stmt = con
                        .prepareStatement("SELECT uname FROM users WHERE uname = (?) AND pass = (?)")) {
            // Apply user input
            stmt.setString(1, uname);
            stmt.setString(2, pass);
            // Execute SQL Query
            stmt.execute();
            // Check if result has rows then return
            return !isNoResult(stmt.getResultSet());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "There was an error in executing the query in DBQuery, userLogin()!\n\n" + e,
                    "Query Execution Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

}

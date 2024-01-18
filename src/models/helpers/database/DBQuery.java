/*
 * Handles all queries executed on Database;
 * 
 * IMPORTANT: always use try-with-resources statement to immediately close the connection
 */

package models.helpers.database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

import javax.swing.JOptionPane;

public class DBQuery {

    private DBManager zavPMSDB;

    DBQuery(DBManager zavPMSDB) {
        this.zavPMSDB = zavPMSDB;
    }

    // Returns TRUE if result of query has no data / rows
    private static boolean isNoResult(ResultSet result) {
        try {
            if (!result.isBeforeFirst()) {
                return true;
            }
            return false;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "There was an error in DBQuery, isNoResult()!\n\n" + e,
                    "DBQuery Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        return true;
    }

    // Returns current date in String format
    private String getDateTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    // Returns level of access type of the user trying to log-in
    public String userLogin(String uname, String pass) {
        // Try-with-resources; immediately closes resources before any catach or finally
        // block is executed
        try (Connection con = this.zavPMSDB.createConnection();
                PreparedStatement stmt = con
                        .prepareStatement(
                                "SELECT level_of_access FROM users INNER JOIN level_of_access ON users.level_of_access_id = level_of_access.id WHERE uname = (?) AND pass = (?)")) {
            // Apply user input
            stmt.setString(1, uname);
            stmt.setString(2, pass);
            // Execute SQL Query
            stmt.execute();

            ResultSet result = stmt.getResultSet();
            if (isNoResult(result)) {
                result.close();
                // Logging log-in attempt to database
                loginAttempt(uname, getDateTime());
                return "";
            } else {
                result.next();
                String loa = result.getString("level_of_access");
                result.close();
                return loa;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "There was an error in executing the query in DBQuery, userLogin()!\n\n" + e,
                    "Query Execution Error",
                    JOptionPane.ERROR_MESSAGE);
            return "";
        }
    }

    // Logs Login Attempt in Database
    private void loginAttempt(String uname, String date) {
        try (Connection con = this.zavPMSDB.createConnection();
                PreparedStatement stmt = con
                        .prepareStatement(
                                "INSERT INTO user_logs (user_id, action_id, date, parameter) VALUES (?, ?, ?, ?);")) {
            // Setting user as SYSTEM
            stmt.setInt(1, 1);
            // Setting action as Login Attempt
            stmt.setInt(2, 1);
            // Applying Date
            stmt.setString(3, date);
            stmt.setString(4, "for user \"" + uname + "\"");
            stmt.execute();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "There was an error in executing the query in DBQuery, loginAttempt()!\n\n" + e,
                    "Query Execution Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

}

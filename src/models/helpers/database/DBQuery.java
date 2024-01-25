/*
 * Handles all queries executed on Database;
 * 
 * IMPORTANT: always use try-with-resources statement to immediately close the connection
 */

package models.helpers.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import models.helpers.DateHelper;
import models.helpers.PopupDialog;

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
            PopupDialog.showErrorDialog(e, "models.helpers.database.DBQuery");
            return true;
        }
    }

    // Returns level of access type of the user trying to log-in
    public String[] userLogin(String uname, String pass) {
        String[] userInfo = { "", "" };
        // Try-with-resources; immediately closes resources before any catach or finally
        // block is executed
        try (Connection con = this.zavPMSDB.createConnection();
                PreparedStatement stmt = con
                        .prepareStatement(
                                "SELECT level_of_access, users.id FROM users INNER JOIN level_of_access ON users.level_of_access_id = level_of_access.id WHERE uname = (?) AND pass = (?)")) {
            // Apply user input
            stmt.setString(1, uname);
            stmt.setString(2, pass);
            // Execute SQL Query
            stmt.execute();

            ResultSet result = stmt.getResultSet();
            // Checking if there are any matches
            if (isNoResult(result)) {
                // Logging log-in attempt to database
                loginAttempt(uname, DateHelper.getCurrentDateTimeString());
                result.close();
                return userInfo;
            } else {
                result.next();
                // Logging log-in to database
                loggedIn(uname, DateHelper.getCurrentDateTimeString());
                // Retrieve level of access and user ID
                userInfo[0] = result.getString("level_of_access");
                userInfo[1] = result.getString("users.id");
                result.close();
                return userInfo;
            }
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
            return userInfo;
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
            PopupDialog.showErrorDialog(e, this.getClass().getName());
        }
    }

    // Logs Login in Database
    private void loggedIn(String uname, String date) {
        try (Connection con = this.zavPMSDB.createConnection();
                PreparedStatement stmt = con
                        .prepareStatement(
                                "INSERT INTO user_logs (user_id, action_id, date, parameter) VALUES (?, ?, ?, ?);")) {
            // Setting user as SYSTEM
            stmt.setInt(1, 1);
            // Setting action as Logged In
            stmt.setInt(2, 2);
            // Applying Date
            stmt.setString(3, date);
            stmt.setString(4, "for user \"" + uname + "\"");
            stmt.execute();
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
        }
    }

}

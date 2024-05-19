/*
 * Handles all queries executed on Database;
 * 
 * IMPORTANT: always use try-with-resources statement to immediately close the connection
 */

package models.helpers.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import enums.UserLogActions;
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
        String[] userInfo = { "", "", "", "" };
        // Try-with-resources; immediately closes resources before any catach or finally
        // block is executed
        try (Connection con = this.zavPMSDB.createConnection();
                PreparedStatement stmt = con
                        .prepareStatement(
                                "SELECT level_of_access, users.id, users.email, account_status_id FROM users INNER JOIN level_of_access ON users.level_of_access_id = level_of_access.id WHERE uname = (?) AND pass = (?)")) {
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
                // Retrieve level of access, user ID and email
                userInfo[0] = result.getString("level_of_access");
                userInfo[1] = result.getString("users.id");
                userInfo[2] = result.getString("users.email");
                userInfo[3] = result.getString("users.account_status_id");
                result.close();
                // Logging initializing OTP authentication to database
                logOTPAuthentication(Integer.parseInt(userInfo[1]), uname,
                        UserLogActions.Actions.INITIATED_OTP.getValue(),
                        DateHelper.getCurrentDateTimeString());
                return userInfo;
            }
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
            return userInfo;
        }
    }

    // Retrieves Username of supplied user_id
    public String getUname(int user_id) {
        try (Connection con = this.zavPMSDB.createConnection();
                PreparedStatement stmt = con
                        .prepareStatement(
                                "SELECT users.uname FROM users WHERE id = (?)")) {
            // Apply user input
            stmt.setString(1, String.valueOf(user_id));
            // Execute SQL Query
            stmt.execute();

            ResultSet result = stmt.getResultSet();
            // Checking if there are any matches
            if (isNoResult(result)) {
                PopupDialog.showCustomErrorDialog("No username has been found for user ID: " + user_id);
                result.close();
                return "error";
            } else {
                result.next();
                // Retrieving Username
                String uname = result.getString("uname");
                result.close();
                return uname;
            }
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
            return "error";
        }
    }

    // Returns true if username exist
    public boolean doesUserNameExist(String uName) {
        try (Connection con = this.zavPMSDB.createConnection();
                PreparedStatement stmt = con
                        .prepareStatement(
                                "SELECT users.uname FROM users WHERE uname = (?)")) {
            // Apply user input
            stmt.setString(1, String.valueOf(uName));
            // Execute SQL Query
            stmt.execute();

            // Retrieve results
            ResultSet result = stmt.getResultSet();
            // Checking if there are any matches
            if (isNoResult(result)) {
                PopupDialog.showCustomErrorDialog("User \"" + uName + "\" does not exist!");
                result.close();
            } else {
                result.next();
                result.close();
                return true;
            }
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
        }
        return false;
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
            stmt.setInt(2, UserLogActions.Actions.LOGIN_ATTEMPT.getValue());
            // Applying Date
            stmt.setString(3, date);
            stmt.setString(4, "for user \"" + uname + "\"");
            stmt.execute();
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
        }
    }

    // Logs OTP Authentication in Database
    public void logOTPAuthentication(int user_id, String uname, int action, String date) {
        try (Connection con = this.zavPMSDB.createConnection();
                PreparedStatement stmt = con
                        .prepareStatement(
                                "INSERT INTO user_logs (user_id, action_id, date, parameter) VALUES (?, ?, ?, ?);")) {
            // Setting user_id
            stmt.setInt(1, user_id);
            // Setting OTP Authentication Action; If initiated or cancelled
            stmt.setInt(2, action);
            // Applying Date
            stmt.setString(3, date);
            stmt.setString(4, "for user \"" + uname + "\"");
            stmt.execute();
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
        }
    }

    // Returns security questions of identified username
    public String[] getSecurityQuestions(String uname) {
        try (Connection con = this.zavPMSDB.createConnection();
                PreparedStatement stmt = con
                        .prepareStatement(
                                "SELECT uname, unique_question, unique_question_answer FROM `zav-pms-db`.users INNER JOIN unique_questions ON `zav-pms-db`.users.unique_question_id = `zav-pms-db`.unique_questions.id WHERE uname = (?);")) {
            // Apply user input
            stmt.setString(1, String.valueOf(uname));
            // Execute SQL Query
            stmt.execute();

            // Retrieve results
            ResultSet result = stmt.getResultSet();
            // Checking if there are any matches
            if (isNoResult(result)) {
                PopupDialog.showCustomErrorDialog("User \"" + uname + "\" does not exist!");
                result.close();
            } else {
                result.next();
                // Retrieving result
                String[] queryResult = { result.getString("uname"), result.getString("unique_question"),
                        result.getString("unique_question_answer") };
                result.close();
                return queryResult;
            }
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
        }
        return new String[] { "", "", "" };
    }

}

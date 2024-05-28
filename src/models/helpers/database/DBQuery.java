/*
 * Handles all queries executed on Database;
 * 
 * IMPORTANT: always use try-with-resources statement to immediately close the connection
 */

package models.helpers.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static java.util.Map.entry;
import java.util.Map;

import enums.UserLogActions;
import models.helpers.DateHelper;
import models.helpers.PopupDialog;
import models.modules.Security;

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

    // Verify user credentials then return user information of user trying to log in
    public Map<String, String> userLogin(String uName, String pass) {
        try (Connection con = this.zavPMSDB.createConnection();
                PreparedStatement stmt = con
                        .prepareStatement(
                                "SELECT users.uname FROM users WHERE BINARY uname = (?) AND BINARY pass = (?);")) {
            // Apply user input
            stmt.setString(1, uName);
            stmt.setString(2, pass);
            // Execute SQL Query
            stmt.execute();

            ResultSet result = stmt.getResultSet();
            // Checking if there are any matches
            if (isNoResult(result)) {
                // Logging log-in attempt to database
                logAction(1, uName, UserLogActions.Actions.LOGIN_ATTEMPT.getValue(),
                        DateHelper.getCurrentDateTimeString());
                result.close();
            } else {
                result.next();
                result.close();
                Map<String, String> userInfo = getUserInfo(uName);
                // Logging initializing OTP authentication to database
                logAction(Integer.parseInt(userInfo.get("id")), uName,
                        UserLogActions.Actions.INITIATED_OTP.getValue(),
                        DateHelper.getCurrentDateTimeString());
                return userInfo;
            }
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
        }
        return Map.of("id", "");
    }

    // Retrieves Username of supplied user_id
    public String getUname(int user_id) {
        try (Connection con = this.zavPMSDB.createConnection();
                PreparedStatement stmt = con
                        .prepareStatement(
                                "SELECT users.uname FROM users WHERE BINARY id = (?);")) {
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

    // Returns info of username
    public Map<String, String> getUserInfo(String uName) {
        try (Connection con = this.zavPMSDB.createConnection();
                PreparedStatement stmt = con
                        .prepareStatement(
                                "SELECT * FROM users WHERE BINARY uname = (?);")) {
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
                Map<String, String> userInfo = Map.ofEntries(
                        entry("id", result.getString("id")),
                        entry("uname", result.getString("uname")),
                        entry("email", result.getString("email")),
                        entry("level_of_access_id", result.getString("level_of_access_id")),
                        entry("fname", result.getString("fname")),
                        entry("lname", result.getString("lname")),
                        entry("account_status_id", result.getString("account_status_id")),
                        entry("unique_question_id", result.getString("unique_question_id")),
                        entry("unique_question_answer", result.getString("unique_question_answer")));
                result.close();
                return userInfo;
            }
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
        }
        // Return empty String
        return (Map<String, String>) Map.of("id", "");
    }

    // Logging Actions to Database
    public void logAction(int user_id, String uname, int action, String date) {
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
            stmt.setString(4, "for user \"" + uname + "\" at " + Security.getSystemName());
            stmt.execute();
            System.out.println("SUCESSFULLY LOGGED ACTION: " + action);
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
        }
    }

    // Updates password of user via Forgot Password
    public boolean updateUserPassword(String username, String newPassword) {
        try (Connection con = this.zavPMSDB.createConnection();
                PreparedStatement stmt = con
                        .prepareStatement(
                                "UPDATE users SET pass = (?) WHERE BINARY uname = (?);")) {
            // Setting user_id
            stmt.setString(1, newPassword);
            // Setting OTP Authentication Action; If initiated or cancelled
            stmt.setString(2, username);
            stmt.execute();

            // Logging password update to database
            logAction(1, username, UserLogActions.Actions.SUCCESS_PASSWORD_RESET.getValue(),
                    DateHelper.getCurrentDateTimeString());
            return true;
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
        }
        return false;
    }

    // Returns security questions of identified username
    public String[] getSecurityQuestions(String uname) {
        try (Connection con = this.zavPMSDB.createConnection();
                PreparedStatement stmt = con
                        .prepareStatement(
                                "SELECT uname, unique_question, unique_question_answer FROM `zav-pms-db`.users INNER JOIN unique_questions ON `zav-pms-db`.users.unique_question_id = `zav-pms-db`.unique_questions.id WHERE BINARY uname = (?);")) {
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
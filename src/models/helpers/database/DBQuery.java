/*
 * Handles all queries executed on Database;
 * 
 * IMPORTANT: always use try-with-resources statement to immediately close the connection
 */

package models.helpers.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import enums.DatabaseLists;
import enums.UserLogActions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.helpers.DateHelper;
import models.helpers.PopupDialog;
import models.modules.Security;
import models.schemas.User;

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
    public User userLogin(String uName, String pass) {
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
                        DateHelper.getCurrentDateTimeString(), "");
                result.close();
            } else {
                result.next();
                result.close();
                User userInfo = getUserInfo(uName);
                return userInfo;
            }
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
        }
        return new User();
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
    public User getUserInfo(String uName) {
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

                // Create new object
                User retrievedUser = new User(Integer.valueOf(result.getString("id")), result.getString("uname"),
                        result.getString("pass"), result.getString("email"),
                        Integer.valueOf(result.getString("level_of_access_id")), result.getString("fname"),
                        result.getString("lname"), Integer.valueOf(result.getString("account_status_id")),
                        Integer.valueOf(result.getString("unique_question_id")),
                        result.getString("unique_question_answer"));

                result.close();

                return retrievedUser;
            }
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
        }
        // Return empty String
        return new User();
    }

    // Logging Actions to Database
    public void logAction(int user_id, String uname, int action, String date, String actionDetails) {
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
            stmt.setString(4, "for user \"" + uname + "\" at " + Security.getSystemName() + ": " + actionDetails);
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
                    DateHelper.getCurrentDateTimeString(), "");
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

    // Returns all of the users registered in database in ObservableList
    public ObservableList<User> getAllUsers() {
        try (Connection con = this.zavPMSDB.createConnection();
                PreparedStatement stmt = con
                        .prepareStatement(
                                "SELECT * FROM users;")) {
            // Execute SQL Query
            stmt.execute();

            ResultSet result = stmt.getResultSet();
            // Checking if there are any matches
            if (isNoResult(result)) {
                PopupDialog.showCustomErrorDialog("Error retrieving list of users");
                result.close();
            } else {
                // Instatiate observable list
                ObservableList<User> myList = FXCollections.observableArrayList();
                // Iterate through all users
                while (result.next()) {
                    // Skipping SYSTEM user
                    if (result.getInt("id") == 1)
                        continue;

                    // Add each user in list
                    myList.add(new User(result.getInt("id"), result.getString("uname"), result.getString("pass"),
                            result.getString("email"),
                            result.getInt("level_of_access_id"), result.getString("fname"), result.getString("lname"),
                            result.getInt("account_status_id"), result.getInt("unique_question_id"),
                            result.getString("unique_question_answer")));
                }
                result.close();
                return myList;
            }
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
        }
        return null;

    }

    // Returns list of items predefined in database
    public ObservableList<String> getListOnDatabase(DatabaseLists.Lists listType) {
        String query = "";

        switch (listType) {
            case DatabaseLists.Lists.SECURITY_QUESTIONS:
                query = "SELECT unique_question FROM unique_questions;";
                break;
            case DatabaseLists.Lists.ACCOUNT_STATUSES:
                query = "SELECT status FROM account_status;";
                break;
            case DatabaseLists.Lists.LEVELS_OF_ACCESS:
                query = "SELECT level_of_access FROM level_of_access;";
            default:
                break;
        }

        ObservableList<String> databaseList = FXCollections.observableArrayList();
        try (Connection con = this.zavPMSDB.createConnection();
                PreparedStatement stmt = con
                        .prepareStatement(
                                query)) {
            // Execute SQL Query
            stmt.execute();

            ResultSet result = stmt.getResultSet();
            // Checking if there are any matches
            if (isNoResult(result)) {
                PopupDialog.showCustomErrorDialog("Error: List is empty");
                result.close();
            } else {
                // Iterate through each question
                while (result.next()) {
                    databaseList.add(result.getString(result.getMetaData().getColumnName(1)));
                }
                result.close();
                // Return question list
                return databaseList;
            }
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
        }
        return null;
    }

    // Updates selected user on database
    public boolean updateUserInfo(User oldUserInfo, User updatedUserInfo, User loggedInUser, String[] changes) {
        try (Connection con = this.zavPMSDB.createConnection();
                PreparedStatement stmt = con
                        .prepareStatement(
                                "UPDATE users SET uname = (?), pass = (?), email = (?), level_of_access_id = (?), account_status_id = (?), unique_question_id = (?), unique_question_answer = (?) WHERE BINARY uname = (?);")) {

            // Putting in values
            stmt.setString(1, updatedUserInfo.getUname());
            stmt.setString(2, updatedUserInfo.getPass());
            stmt.setString(3, updatedUserInfo.getEmail());
            stmt.setInt(4, updatedUserInfo.getLevel_of_access_id());
            stmt.setInt(5, updatedUserInfo.getAccount_status_id());
            stmt.setInt(6, updatedUserInfo.getUniqueQuestionID());
            stmt.setString(7, updatedUserInfo.getUniqueQuestionAnswer());
            stmt.setString(8, oldUserInfo.getUname());

            stmt.execute();

            String changeMessage = "";
            // Getting changes in array
            for (String change : changes) {
                changeMessage += (change + ", ");
            }

            // Logging account update to database
            logAction(loggedInUser.getId(), loggedInUser.getUname(), UserLogActions.Actions.UPDATED_USER_ACCOUNT.getValue(),
                    DateHelper.getCurrentDateTimeString(),
                    "updated account details of user " + "\"" + updatedUserInfo.getUname() + "\"; " + changeMessage);
            // Return true if success
            return true;
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
        }
        // Return false if error
        return false;
    }

}
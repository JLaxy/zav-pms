/*
 * Handles all queries executed on Database;
 * 
 * IMPORTANT: always use try-with-resources statement to immediately close the connection
 */

package models.helpers.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

import enums.DatabaseLists;
import enums.UserLogActions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.helpers.DateHelper;
import models.helpers.PopupDialog;
import models.modules.Security;
import models.schemas.Stock;
import models.schemas.StockType;
import models.schemas.User;
import models.schemas.UserLog;
import models.schemas.DatabaseLog;

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

    public boolean doesEmailExist(String email) {
        ObservableList<User> listOfUsers = getAllUsers(null);

        for (User user : listOfUsers) {
            if (email.compareTo(user.getEmail()) == 0)
                return true;
        }
        return false;
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

    // Logs database action to database
    public void logDatabaseAction(User userInfo, String date, int backupTypeId) {
        try (Connection con = this.zavPMSDB.createConnection();
                PreparedStatement stmt = con
                        .prepareStatement(
                                "INSERT INTO backup_log (user_id, date, system_name, backup_type_id) VALUES (?, ?, ?, ?);")) {
            // Applying values
            stmt.setInt(1, userInfo.getId());
            stmt.setString(2, DateHelper.getCurrentDateTimeString());
            // Applying Date
            stmt.setString(3, Security.getSystemName());
            stmt.setInt(4, backupTypeId);
            stmt.execute();
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
        }
    }

    public ObservableList<DatabaseLog> getDatabaseLogs() {
        ObservableList<DatabaseLog> myList = FXCollections.observableArrayList();
        try (Connection con = this.zavPMSDB.createConnection();
                PreparedStatement stmt = con
                        .prepareStatement(
                                "SELECT * FROM backup_log;")) {
            // Execute SQL Query
            stmt.execute();

            ResultSet result = stmt.getResultSet();
            // Checking if there are any matches
            if (isNoResult(result)) {
                PopupDialog.showCustomErrorDialog("Error retrieving backup_logs");
                result.close();
            } else {
                while (result.next()) {
                    myList.add(new DatabaseLog(result.getInt("id"), result.getInt("user_id"),
                            result.getInt("backup_type_id"), result.getString("date"),
                            result.getString("system_name")));
                }
                result.close();
            }
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
        }
        // Return list
        return myList;
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
    public ObservableList<User> getAllUsers(String userQuery) {
        String query = "";

        if (userQuery == null)
            query = "SELECT * FROM users;";
        else
            query = "SELECT * FROM users WHERE uname LIKE ?";

        try (Connection con = this.zavPMSDB.createConnection();
                PreparedStatement stmt = con
                        .prepareStatement(
                                query)) {

            if (userQuery != null)
                stmt.setString(1, "%" + userQuery + "%");

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

                    myList.add(new User(result.getInt("id"), result.getString("uname"), result.getString("pass"),
                            result.getString("email"),
                            result.getInt("level_of_access_id"), result.getString("fname"),
                            result.getString("lname"),
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
                break;
            case DatabaseLists.Lists.UNIT_MEASURE:
                query = "SELECT unit FROM unit_measure;";
                break;
            case DatabaseLists.Lists.STOCK_TYPE:
                query = "SELECT type FROM stock_type;";
                break;
            default:
                break;
        }

        ObservableList<String> databaseList = FXCollections.observableArrayList();
        try (Connection con = this.zavPMSDB.createConnection();
                PreparedStatement stmt = con
                        .prepareStatement(query)) {
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
                                "UPDATE users SET uname = (?), pass = (?), email = (?), fname = (?), lname = (?), level_of_access_id = (?), account_status_id = (?), unique_question_id = (?), unique_question_answer = (?) WHERE BINARY uname = (?);")) {

            // Putting in values
            stmt.setString(1, updatedUserInfo.getUname());
            stmt.setString(2, updatedUserInfo.getPass());
            stmt.setString(3, updatedUserInfo.getEmail());
            stmt.setString(4, updatedUserInfo.getFName());
            stmt.setString(5, updatedUserInfo.getLName());
            stmt.setInt(6, updatedUserInfo.getLevel_of_access_id());
            stmt.setInt(7, updatedUserInfo.getAccount_status_id());
            stmt.setInt(8, updatedUserInfo.getUniqueQuestionID());
            stmt.setString(9, updatedUserInfo.getUniqueQuestionAnswer());
            stmt.setString(10, oldUserInfo.getUname());

            stmt.execute();

            String changeMessage = "";
            // Getting changes in array
            for (String change : changes) {
                changeMessage += (change + ", ");
            }

            // Logging account update to database
            logAction(loggedInUser.getId(), loggedInUser.getUname(),
                    UserLogActions.Actions.UPDATED_USER_ACCOUNT.getValue(),
                    DateHelper.getCurrentDateTimeString(),
                    "updated account details of user " + "\"" + oldUserInfo.getUname() + "\"; " + changeMessage);
            // Return true if success
            return true;
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
        }
        // Return false if error
        return false;
    }

    // Save new user on database
    public boolean saveNewUser(User newUser, User loggedInUserInfo) {
        try (Connection con = this.zavPMSDB.createConnection();
                PreparedStatement stmt = con
                        .prepareStatement(
                                "INSERT INTO users (uname, pass, email, level_of_access_id, fname, lname, account_status_id, unique_question_id, unique_question_answer) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);")) {

            // Setting user info
            stmt.setString(1, newUser.getUname());
            stmt.setString(2, newUser.getPass());
            stmt.setString(3, newUser.getEmail());
            stmt.setInt(4, newUser.getLevel_of_access_id());
            stmt.setString(5, newUser.getFName());
            stmt.setString(6, newUser.getLName());
            stmt.setInt(7, newUser.getAccount_status_id());
            stmt.setInt(8, newUser.getUniqueQuestionID());
            stmt.setString(9, newUser.getUniqueQuestionAnswer());

            stmt.execute();

            // Log creating new user in database
            logAction(loggedInUserInfo.getId(), loggedInUserInfo.getUname(),
                    UserLogActions.Actions.REGISTERED_NEW_USER.getValue(),
                    DateHelper.getCurrentDateTimeString(), "registered new user \"" + newUser.getUname() + "\"");
            return true;
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
        }
        return false;
    }

    // Returns all of the user logs stored in the database
    public ObservableList<UserLog> getUserLogs(String uname, String selectedDate) {
        // Instatiate observable list
        ObservableList<UserLog> myList = FXCollections.observableArrayList();
        String query = "";
        // If not searching for a specific user
        if (uname == null)
            query = "SELECT * FROM user_logs WHERE date LIKE '" + selectedDate + "%';";
        else
            query = "SELECT * FROM user_logs INNER JOIN users ON users.id = user_logs.user_id WHERE date LIKE '"
                    + selectedDate + "%' AND uname LIKE ?;";

        try (Connection con = this.zavPMSDB.createConnection();
                PreparedStatement stmt = con
                        .prepareStatement(
                                query)) {

            // If searching for a specific user
            if (uname != null) {
                stmt.setString(1, "%" + uname + "%");
            }
            // Execute SQL Query
            stmt.execute();

            ResultSet result = stmt.getResultSet();
            // Checking if there are any matches
            if (isNoResult(result)) {
                System.out.println("no logs for date " + selectedDate);
                result.close();
            } else {
                // Iterate through all user logs
                while (result.next()) {
                    UserLog log = new UserLog(result.getInt("id"), result.getInt("user_id"), result.getInt("action_id"),
                            result.getString("date"), result.getString("parameter"));
                    // Get the username
                    log.setUname(getUname(log.getUser_id()));
                    // Add to list
                    myList.add(log);
                }
                result.close();
            }
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
        }
        return myList;
    }

    // Save new stock type on database
    public boolean saveNewStockType(StockType newStockType, User loggedInUserInfo) {
        try (Connection con = this.zavPMSDB.createConnection();
                PreparedStatement stmt = con
                        .prepareStatement(
                                "INSERT INTO stock_type (type, default_expiration) VALUES (?, ?);")) {

            // Setting stock type informations
            stmt.setString(1, newStockType.getType());
            stmt.setInt(2, newStockType.getDefault_expiration());

            stmt.execute();

            // Log creating new stock type in database
            logAction(loggedInUserInfo.getId(), loggedInUserInfo.getUname(),
                    UserLogActions.Actions.REGISTERED_NEW_STOCK_TYPE.getValue(),
                    DateHelper.getCurrentDateTimeString(),
                    "registered new stock type \"" + newStockType.getType() + "\"");
            return true;
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
        }
        return false;
    }

    // Save new stock on database
    public boolean saveNewStock(Stock newStock, User loggedInUserInfo) {
        try (Connection con = this.zavPMSDB.createConnection();
                PreparedStatement stmt = con
                        .prepareStatement(
                                "INSERT INTO stock (stock_name, quantity, unit_measure_id, stock_type_id, critical_level, isVoided) VALUES (?, ?, ?, ?, ?, ?);")) {

            // Setting stock info
            stmt.setString(1, newStock.getStock_name());
            stmt.setInt(2, newStock.getQuantity());
            stmt.setInt(3, newStock.getUnit_measure_id());
            stmt.setInt(4, newStock.getStock_type_id());
            stmt.setInt(5, newStock.getCritical_level());
            stmt.setBoolean(6, newStock.getisVoided());

            stmt.execute();

            // Log creating new user in database
            logAction(loggedInUserInfo.getId(), loggedInUserInfo.getUname(),
                    UserLogActions.Actions.REGISTERED_NEW_STOCK.getValue(),
                    DateHelper.getCurrentDateTimeString(), "registered new user \"" + newStock.getStock_name() + "\"");
            return true;
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
        }
        return false;
    }

    public ObservableList<Stock> getStocks(String stockName) {
        String query = "";

        if (stockName == null)
            query = "SELECT * FROM stock WHERE isVoided = 0;";
        else
            query = "SELECT * FROM stock WHERE isVoided = 0 AND stock_name LIKE ?;";

        ObservableList<Stock> myList = FXCollections.observableArrayList();
        try (Connection con = this.zavPMSDB.createConnection();
                PreparedStatement stmt = con
                        .prepareStatement(
                                query)) {

            if (stockName != null) {
                stmt.setString(1, ("%" + stockName + "%"));
            }

            // Execute SQL Query
            stmt.execute();

            ResultSet result = stmt.getResultSet();
            // Checking if there are any matches
            if (isNoResult(result)) {
                result.close();
            } else {
                while (result.next()) {
                    myList.add(new Stock(result.getInt("id"), result.getString("stock_name"), result.getInt("quantity"),
                            result.getInt("unit_measure_id"), result.getInt("stock_type_id"),
                            result.getInt("critical_level"), result.getInt("isVoided") == 0 ? false : true));
                }
                result.close();
            }
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
        }
        // Return list
        return myList;
    }

    public boolean editStock(Stock newStock, User loggedInUser, String[] changes) {
        try (Connection con = this.zavPMSDB.createConnection();
                PreparedStatement stmt = con
                        .prepareStatement(
                                "UPDATE stock SET stock_name = (?), quantity = (?), unit_measure_id = (?), stock_type_id = (?), critical_level = (?), isVoided = (?) WHERE id = (?);")) {

            // Putting in values
            stmt.setString(1, newStock.getStock_name());
            stmt.setInt(2, newStock.getQuantity());
            stmt.setInt(3, newStock.getUnit_measure_id());
            stmt.setInt(4, newStock.getStock_type_id());
            stmt.setInt(5, newStock.getCritical_level());
            stmt.setInt(6, newStock.getisVoided() ? 1 : 0);
            stmt.setInt(7, newStock.getId());

            stmt.execute();

            String changeMessage = "";
            // Getting changes in array
            for (String change : changes) {
                changeMessage += (change + ", ");
            }

            logAction(loggedInUser.getId(), loggedInUser.getUname(), UserLogActions.Actions.EDITED_STOCK.getValue(),
                    DateHelper.getCurrentDateTimeString(),
                    "updated details of stock \"" + newStock.getStock_name() + "\";" + changeMessage);

            // Return true if success
            return true;
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
        }
        // Return false if error
        return false;
    }

    public boolean logStockProductPurchase(Stock selectedStock, int quantity, float totalCost, LocalDate datePurchased,
            LocalDate expiryDate, int stockProductTypeID, User loggedInUser) {
        try (Connection con = this.zavPMSDB.createConnection();
                PreparedStatement stmt = con
                        .prepareStatement(
                                "INSERT INTO stock_product_expenses (stock_id, quantity, total_cost, date_purchased, stock_product_type_id, expiry_date) VALUES (?, ?, ?, ?, ?, ?);")) {

            // Setting stock info
            stmt.setInt(1, selectedStock.getId());
            stmt.setInt(2, quantity);
            stmt.setFloat(3, totalCost);
            stmt.setString(4, DateHelper.dateToString(datePurchased));
            stmt.setInt(5, stockProductTypeID);
            stmt.setString(6, DateHelper.dateToString(expiryDate));

            stmt.execute();

            // Log creating new user in database
            logAction(loggedInUser.getId(), loggedInUser.getUname(),
                    UserLogActions.Actions.LOGGED_STOCK_PRODUCT_PURCHASE.getValue(),
                    DateHelper.getCurrentDateTimeString(),
                    "registered stock/product purchase of \"" + selectedStock.getStock_name() + "\"");
            return true;
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
        }
        return false;
    }

}
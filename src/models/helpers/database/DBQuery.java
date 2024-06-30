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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

import com.mysql.cj.x.protobuf.MysqlxCrud.Order;

import enums.DatabaseLists;
import enums.PreferredUnits;
import enums.StockProductType;
import enums.UserLogActions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.helpers.DateHelper;
import models.helpers.PopupDialog;
import models.helpers.NumberHelper;
import models.modules.Security;
import models.schemas.Stock;
import models.schemas.StockType;
import models.schemas.User;
import models.schemas.UserLog;
import models.schemas.DatabaseLog;
import models.schemas.DrinkVariant;
import models.schemas.DeprecatedItem;
import models.schemas.FoodVariant;
import models.schemas.PurchasedInventoryItem;

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
            case DatabaseLists.Lists.PRODUCT_SERVING_SIZE:
                query = "SELECT size FROM product_serving_size";
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
            stmt.setDouble(2, newStock.getQuantity());
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
            stmt.setDouble(2, newStock.getQuantity());
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

    public boolean logStockProductPurchase(int stockProductID, int quantity, double totalCost, LocalDate datePurchased,
            LocalDate expiryDate, int stockProductTypeID, User loggedInUser, String stockProductName) {
        try (Connection con = this.zavPMSDB.createConnection();
                PreparedStatement stmt = con
                        .prepareStatement(
                                "INSERT INTO stock_product_expenses (stock_id, quantity, total_cost, date_purchased, stock_product_type_id, expiry_date) VALUES (?, ?, ?, ?, ?, ?);")) {

            // Setting stock info
            stmt.setInt(1, stockProductID);
            stmt.setInt(2, quantity);
            stmt.setDouble(3, totalCost);
            stmt.setString(4, DateHelper.dateToString(datePurchased));
            stmt.setInt(5, stockProductTypeID);
            stmt.setString(6, DateHelper.dateToString(expiryDate));

            stmt.execute();

            // Log creating new user in database
            logAction(loggedInUser.getId(), loggedInUser.getUname(),
                    UserLogActions.Actions.LOGGED_STOCK_PRODUCT_PURCHASE.getValue(),
                    DateHelper.getCurrentDateTimeString(),
                    "registered stock/product purchase of \"" + stockProductName + "\"");
            return true;
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
        }
        return false;
    }

    public ObservableList<PurchasedInventoryItem> getInventoryPurchaseHistory(String inventoryItem) {
        // Query for stocks
        String stockQuery = "";
        // Query for beverage product
        String beverageQuery = "";

        if (inventoryItem == null) {
            stockQuery = "SELECT stock_product_expenses.id, stock_id, stock_product_expenses.quantity, total_cost, date_purchased, stock_product_type_id, expiry_date, unit_measure.unit, stock_name FROM `zav-pms-db`.stock_product_expenses JOIN stock ON stock.id = stock_product_expenses.stock_id JOIN unit_measure ON unit_measure.id = stock.unit_measure_id ORDER BY date_purchased DESC;";
            beverageQuery = "SELECT stock_product_expenses.id, drink_product.id, stock_product_expenses.quantity, total_cost, date_purchased, products_name.stock_product_type_id, expiry_date, product_name, size, drink_product.preferred_unit_id FROM `zav-pms-db`.drink_product JOIN products_name ON drink_product.products_name_id = products_name.id JOIN stock_product_expenses ON stock_product_expenses.stock_id = drink_product.id ORDER BY date_purchased DESC;";
        } else {
            stockQuery = "SELECT stock_product_expenses.id, stock_id, stock_product_expenses.quantity, total_cost, date_purchased, stock_product_type_id, expiry_date, unit_measure.unit, stock_name FROM `zav-pms-db`.stock_product_expenses JOIN stock ON stock.id = stock_product_expenses.stock_id JOIN unit_measure ON unit_measure.id = stock.unit_measure_id WHERE stock_name LIKE ? ORDER BY date_purchased DESC;";
            beverageQuery = "SELECT stock_product_expenses.id, drink_product.id, stock_product_expenses.quantity, total_cost, date_purchased, products_name.stock_product_type_id, expiry_date, product_name, size, drink_product.preferred_unit_id FROM `zav-pms-db`.drink_product JOIN products_name ON drink_product.products_name_id = products_name.id JOIN stock_product_expenses ON stock_product_expenses.stock_id = drink_product.id WHERE product_name LIKE ? ORDER BY date_purchased DESC;";
        }

        ObservableList<PurchasedInventoryItem> resultList = FXCollections.observableArrayList();
        try (Connection con = this.zavPMSDB.createConnection();
                PreparedStatement stmt = con
                        .prepareStatement(
                                stockQuery);
                PreparedStatement stmt2 = con
                        .prepareStatement(
                                beverageQuery)) {

            if (inventoryItem != null) {
                stmt.setString(1, ("%" + inventoryItem + "%"));
                stmt2.setString(1, ("%" + inventoryItem + "%"));
            }
            // Execute SQL Query
            stmt.execute();
            stmt2.execute();

            // Retrieving results
            ResultSet stockResult = stmt.getResultSet();
            ResultSet beverageResult = stmt2.getResultSet();

            // If has no result, close then skip
            if (isNoResult(stockResult)) {
                System.out.println("no result for stocks");
                stockResult.close();
            }
            // Else, process
            else {
                // Processing Stock Result
                while (stockResult.next()) {
                    // If inventory item is a stock
                    if (stockResult.getInt("stock_product_type_id") == 2) {
                        // Add to list
                        resultList.add(new PurchasedInventoryItem(stockResult.getInt("id"),
                                stockResult.getInt("stock_id"), stockResult.getInt("quantity"),
                                stockResult.getFloat("total_cost"), stockResult.getString("date_purchased"),
                                stockResult.getInt("stock_product_type_id"), stockResult.getString("expiry_date"),
                                stockResult.getString("stock_name"), stockResult.getString("unit"), null));
                    }
                }
                stockResult.close();
            }

            // If has no result, close then skip
            if (isNoResult(beverageResult)) {
                System.out.println("no result for beverage");
                beverageResult.close();
            } else {
                // Processing beverage result
                while (beverageResult.next()) {
                    // If inventory item is a beverage
                    if (beverageResult.getInt("stock_product_type_id") == 1) {

                        // Format size into string
                        String size = beverageResult.getInt("size") + "mL";
                        if (beverageResult.getInt("preferred_unit_id") == 3)
                            size = NumberHelper.mililiterToLiter(beverageResult.getInt("size")) + "L";

                        // Add to list
                        resultList.add(new PurchasedInventoryItem(beverageResult.getInt(1),
                                beverageResult.getInt(2), beverageResult.getInt("quantity"),
                                beverageResult.getFloat("total_cost"), beverageResult.getString("date_purchased"),
                                beverageResult.getInt("stock_product_type_id"),
                                beverageResult.getString("expiry_date"),
                                beverageResult.getString("product_name"), "bottle",
                                size));
                    }
                }
                beverageResult.close();
            }
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
        }

        // Sort by date descending
        Comparator<PurchasedInventoryItem> sorter = Comparator.comparing(PurchasedInventoryItem::getDate_purchased);
        Collections.sort(resultList, sorter.reversed());

        // Return list
        return resultList;
    }

    // Returns list of beverage products; used for combobox
    public ObservableList<String> getBeverageProducts() {
        ObservableList<String> myList = FXCollections.observableArrayList();
        try (Connection con = this.zavPMSDB.createConnection();
                PreparedStatement stmt = con
                        .prepareStatement(
                                "SELECT * FROM products_name WHERE stock_product_type_id = 1;")) {
            // Execute SQL Query
            stmt.execute();

            ResultSet result = stmt.getResultSet();
            // Checking if there are any matches
            if (isNoResult(result)) {
                result.close();
            } else {
                // Iterate through result then add to list
                while (result.next()) {
                    myList.add(result.getString("product_name"));
                }
                result.close();
            }
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
        }
        // Return list
        return myList;
    }

    public ObservableList<String> getFoodProducts() {
        ObservableList<String> myList = FXCollections.observableArrayList();
        try (Connection con = this.zavPMSDB.createConnection();
                PreparedStatement stmt = con
                        .prepareStatement(
                                "SELECT * FROM products_name WHERE stock_product_type_id = 3;")) {
            // Execute SQL Query
            stmt.execute();

            ResultSet result = stmt.getResultSet();
            // Checking if there are any matches
            if (isNoResult(result)) {
                result.close();
            } else {
                // Iterate through result then add to list
                while (result.next()) {
                    myList.add(result.getString("product_name"));
                }
                result.close();
            }
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
        }
        // Return list
        return myList;
    }

    // Returns list of beverage product object; used in beverage product view;
    // overloaded method
    public ObservableList<DrinkVariant> getBeverageProducts(String beverage) {
        String query = "";

        if (beverage == null)
            query = "SELECT * FROM `zav-pms-db`.drink_product JOIN products_name ON drink_product.products_name_id = products_name.id WHERE isVoided = 0;";
        else
            query = "SELECT * FROM `zav-pms-db`.drink_product JOIN products_name ON drink_product.products_name_id = products_name.id WHERE isVoided = 0 AND products_name.product_name LIKE ?;";

        ObservableList<DrinkVariant> myList = FXCollections.observableArrayList();
        try (Connection con = this.zavPMSDB.createConnection();
                PreparedStatement stmt = con
                        .prepareStatement(
                                query)) {
            // Apply if there is a query
            if (beverage != null)
                stmt.setString(1, "%" + beverage + "%");

            // Execute SQL Query
            stmt.execute();

            ResultSet result = stmt.getResultSet();
            // Checking if there are any matches
            if (isNoResult(result)) {
                result.close();
            } else {
                // Iterate through result then add to list
                while (result.next()) {
                    myList.add(new DrinkVariant(result.getInt("id"), result.getString("product_name"),
                            result.getInt("products_name_id"),
                            result.getDouble("size"), result.getDouble("price"), result.getInt("available_count"),
                            result.getInt("critical_level"), result.getBoolean("isVoided"),
                            result.getDouble("discounted_price"), result.getInt("preferred_unit_id")));
                }
                result.close();
            }
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
        }
        // Return list
        return myList;
    }

    // Register new product in database
    public boolean registerNewProduct(User loggedInUserInfo, String newProduct, StockProductType.Type type) {
        try (Connection con = this.zavPMSDB.createConnection();
                PreparedStatement stmt = con
                        .prepareStatement(
                                "INSERT INTO products_name (product_name, stock_product_type_id) VALUES (?, ?);")) {

            // Setting user info
            stmt.setString(1, newProduct);
            stmt.setInt(2, type.getValue());

            stmt.execute();

            UserLogActions.Actions action = null;

            if (type == StockProductType.Type.BEVERAGE)
                action = UserLogActions.Actions.REGISTERED_NEW_BEVERAGE_PRODUCT;
            else if (type == StockProductType.Type.FOOD)
                action = UserLogActions.Actions.REGISTERED_NEW_FOOD_PRODUCT;

            // Log creating new user in database
            logAction(loggedInUserInfo.getId(), loggedInUserInfo.getUname(),
                    action.getValue(),
                    DateHelper.getCurrentDateTimeString(), "registered new product \"" + newProduct + "\"");
            return true;
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
        }
        return false;
    }

    // Register new variant of product in database
    public boolean registerNewDrinkVariant(User loggedInUserInfo, DrinkVariant drinkProduct, String productName) {
        try (Connection con = this.zavPMSDB.createConnection();
                PreparedStatement stmt = con
                        .prepareStatement(
                                "INSERT INTO drink_product (products_name_id, size, price, available_count, critical_level, isVoided, discounted_price, preferred_unit_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?);")) {

            // Setting user info
            stmt.setInt(1, drinkProduct.getProducts_name_id());
            stmt.setDouble(2, drinkProduct.getSize());
            stmt.setDouble(3, drinkProduct.getPrice());
            stmt.setInt(4, drinkProduct.getAvailable_count());
            stmt.setInt(5, drinkProduct.getCritical_level());
            stmt.setBoolean(6, drinkProduct.isVoided());
            stmt.setDouble(7, drinkProduct.getDiscounted_price());
            stmt.setDouble(8, drinkProduct.getPreferred_unit_id());

            stmt.execute();

            // Log creating new user in database
            logAction(loggedInUserInfo.getId(), loggedInUserInfo.getUname(),
                    UserLogActions.Actions.REGISTERED_NEW_BEVERAGE_PRODUCT_VARIANT.getValue(),
                    DateHelper.getCurrentDateTimeString(), "registered new product variant of \"" + productName + "\"");
            return true;
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
        }
        return false;
    }

    public boolean editBeverage(DrinkVariant oldBeverage, DrinkVariant newBeverage, User loggedInUser,
            String[] changes) {
        try (Connection con = this.zavPMSDB.createConnection();
                PreparedStatement stmt = con
                        .prepareStatement(
                                "UPDATE drink_product SET available_count = (?), isVoided = (?) WHERE id = (?);")) {

            // Putting in values
            stmt.setInt(1, newBeverage.getAvailable_count());
            stmt.setInt(2, newBeverage.isVoided() ? 1 : 0);
            stmt.setInt(3, newBeverage.getId());

            stmt.execute();

            String changeMessage = "";
            // Getting changes in array
            for (String change : changes) {
                changeMessage += (change + ", ");
            }

            logAction(loggedInUser.getId(), loggedInUser.getUname(),
                    UserLogActions.Actions.EDITED_BEVERAGE_VARIANT.getValue(),
                    DateHelper.getCurrentDateTimeString(),
                    "updated details of beverage variant \"" + oldBeverage.getProduct_name()
                            + " " + newBeverage.getSize_string() + "\";" + changeMessage);

            // Return true if success
            return true;
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
        }
        // Return false if error
        return false;
    }

    public boolean registerNewFoodVariant(User loggedInUser, FoodVariant foodProduct, String productName) {
        try (Connection con = this.zavPMSDB.createConnection();
                PreparedStatement stmt = con
                        .prepareStatement(
                                "INSERT INTO food_product (products_name_id, regular_price, serving_size_id, available_count, discounted_price, isVoided) VALUES (?, ?, ?, ?, ?, ?);")) {

            // Setting user info
            stmt.setInt(1, foodProduct.getProduct_name_id());
            stmt.setDouble(2, foodProduct.getRegular_price());
            stmt.setInt(3, foodProduct.getServing_size_id());
            stmt.setInt(4, foodProduct.getAvailable_count());
            stmt.setDouble(5, foodProduct.getDiscounted_price());
            stmt.setBoolean(6, foodProduct.getIs_voided());

            stmt.execute();

            // Log creating new user in database
            logAction(loggedInUser.getId(), loggedInUser.getUname(),
                    UserLogActions.Actions.REGISTERED_NEW_FOOD_PRODUCT_VARIANT.getValue(),
                    DateHelper.getCurrentDateTimeString(), "registered new product variant of \"" + productName + "\"");
            return true;
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
        }
        return false;
    }

    public boolean registerRequiredStocks(User loggedInUser, ObservableList<Stock> requiredStocks,
            FoodVariant foodVariant) {
        ArrayList<String> addedStocks = new ArrayList<String>();
        try (Connection con = this.zavPMSDB.createConnection();
                PreparedStatement stmt = con
                        .prepareStatement(
                                "INSERT INTO stock_required (food_product_id, stock_id, quantity) VALUES (?, ?, ?);")) {

            for (Stock requiredStock : requiredStocks) {
                stmt.setInt(1, foodVariant.getId());
                stmt.setInt(2, requiredStock.getId());
                stmt.setDouble(3, requiredStock.getQuantity());
                stmt.execute();

                addedStocks.add(requiredStock.getStock_name());
            }

            String stocksInComma = "";

            // Put into string separted by commas
            for (int i = 0; i < addedStocks.size(); i++) {
                if (i == 0) {
                    stocksInComma += addedStocks.get(i);
                    continue;
                }
                stocksInComma += ", " + addedStocks.get(i);
            }

            // Log creating new user in database
            logAction(loggedInUser.getId(), loggedInUser.getUname(),
                    UserLogActions.Actions.ADDED_STOCKS_REQUIRED_ON_FOOD_PRODUCT_VARIANT.getValue(),
                    DateHelper.getCurrentDateTimeString(),
                    "added stock(s) required on new food product variant \""
                            + getProductName(foodVariant.getProduct_name_id()) + "\"; " + stocksInComma);
            return true;
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
        }
        return false;
    }

    public String getProductName(int id) {
        String productName = null;
        try (Connection con = this.zavPMSDB.createConnection();
                PreparedStatement stmt = con
                        .prepareStatement(
                                "SELECT * FROM products_name WHERE id = (?);")) {
            stmt.setInt(1, id);
            // Execute SQL Query
            stmt.execute();

            ResultSet result = stmt.getResultSet();
            // Checking if there are any matches
            if (isNoResult(result)) {
                result.close();
            } else {
                result.next();
                productName = result.getString("product_name");
                result.close();
            }
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
        }
        // Return list
        return productName;
    }

    // Returns specific food variant object
    public FoodVariant getFoodVariant(int products_name_id, int serving_size_id) {
        FoodVariant retrievedFoodVariant = null;
        try (Connection con = this.zavPMSDB.createConnection();
                PreparedStatement stmt = con
                        .prepareStatement(
                                "SELECT * FROM food_product WHERE products_name_id = (?) AND serving_size_id = (?) AND isVoided = 0")) {
            stmt.setInt(1, products_name_id);
            stmt.setInt(2, serving_size_id);
            // Execute SQL Query
            stmt.execute();

            ResultSet result = stmt.getResultSet();
            // Checking if there are any matches
            if (isNoResult(result)) {
                result.close();
            } else {
                result.next();
                retrievedFoodVariant = new FoodVariant(result.getInt("id"), result.getInt("products_name_id"),
                        result.getDouble("regular_price"), result.getInt("serving_size_id"),
                        result.getInt("available_count"), result.getDouble("discounted_price"), false);
                result.close();
            }
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
        }
        return retrievedFoodVariant;
    }

    public ObservableList<FoodVariant> getFoodProducts(String product) {
        ObservableList<FoodVariant> listOfFood = FXCollections.observableArrayList();
        String query = "";

        if (product == null)
            query = "SELECT food_product.id, products_name_id, regular_price, serving_size_id, available_count, discounted_price, isVoided, products_name.product_name FROM `zav-pms-db`.food_product JOIN products_name ON products_name_id = products_name.id  WHERE isVoided = 0;";
        else
            query = "SELECT food_product.id, products_name_id, regular_price, serving_size_id, available_count, discounted_price, isVoided, products_name.product_name FROM `zav-pms-db`.food_product JOIN products_name ON products_name_id = products_name.id  WHERE isVoided = 0 AND product_name LIKE ?";

        try (Connection con = this.zavPMSDB.createConnection();
                PreparedStatement stmt = con
                        .prepareStatement(query)) {
            if (product != null)
                stmt.setString(1, "%" + product + "%");
            // Execute SQL Query
            stmt.execute();

            ResultSet result = stmt.getResultSet();
            // Checking if there are any matches
            if (isNoResult(result)) {
                result.close();
            } else {
                while (result.next()) {
                    FoodVariant newItem = new FoodVariant(result.getInt("id"), result.getInt("products_name_id"),
                            result.getDouble("regular_price"), result.getInt("serving_size_id"),
                            result.getInt("available_count"), result.getDouble("discounted_price"), false);
                    newItem.setFood_name(result.getString("product_name"));
                    listOfFood.add(newItem);
                }
                result.close();
            }
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
        }
        return listOfFood;
    }

    // Retrieves id of product name; returns -1 if does not exist
    public int getProductNameId(String productName) {
        int retrievedID = -1;
        try (Connection con = this.zavPMSDB.createConnection();
                PreparedStatement stmt = con
                        .prepareStatement(
                                "SELECT * FROM products_name WHERE product_name = ?")) {
            stmt.setString(1, productName);
            // Execute SQL Query
            stmt.execute();

            ResultSet result = stmt.getResultSet();
            // Checking if there are any matches
            if (isNoResult(result)) {
                result.close();
            } else {
                result.next();
                retrievedID = result.getInt("id");
                result.close();
            }
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
        }
        return retrievedID;
    }

    public boolean editFoodVariant(FoodVariant selectedFood, FoodVariant updatedFood, User loggedInUser,
            String[] changes) {
        try (Connection con = this.zavPMSDB.createConnection();
                PreparedStatement stmt = con
                        .prepareStatement(
                                "UPDATE food_product SET available_count = (?), isVoided = (?) WHERE id = (?);")) {

            // Putting in values
            stmt.setInt(1, updatedFood.getAvailable_count());
            stmt.setInt(2, updatedFood.getIs_voided() ? 1 : 0);
            stmt.setInt(3, selectedFood.getId());

            stmt.execute();

            String changeMessage = "";
            // Getting changes in array
            for (String change : changes) {
                changeMessage += (change + ", ");
            }

            logAction(loggedInUser.getId(), loggedInUser.getUname(),
                    UserLogActions.Actions.EDITED_FOOD_VARIANT.getValue(),
                    DateHelper.getCurrentDateTimeString(),
                    "updated details of food variant \"" + selectedFood.getFood_name()
                            + " " + updatedFood.getServing_size_id_string() + "\";" + changeMessage);

            // Return true if success
            return true;
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
        }
        // Return false if error
        return false;
    }

    // Returns Map of all deprecated (expired / expiring) inventory items from
    // database
    public Map<String, Object> getDeprecatedItems(String deprecatedItemType) {
        // Contains all of the items that are expiring
        Map<String, Object> deprecatedItems = new HashMap<String, Object>();

        String query = "";
        if (deprecatedItemType.compareTo("EXPIRED") == 0)
            query = "SELECT * FROM `zav-pms-db`.stock_product_expenses WHERE (stock_product_type_id = 1 OR stock_product_type_id = 2) AND datediff(expiry_date, now()) < 1;";
        else if (deprecatedItemType.compareTo("EXPIRING") == 0)
            query = "SELECT * FROM `zav-pms-db`.stock_product_expenses WHERE (stock_product_type_id = 1 OR stock_product_type_id = 2) AND datediff(expiry_date, now()) < 3 AND datediff(expiry_date, now()) > 0;";
        // Return if invalid
        else
            return deprecatedItems;

        try (Connection con = this.zavPMSDB.createConnection();
                PreparedStatement stmt = con.prepareStatement(query)) {
            // Execute SQL Query
            stmt.execute();

            ResultSet result = stmt.getResultSet();
            // Checking if there are any matches
            if (isNoResult(result)) {
                result.close();
            } else {
                // Iterate through results the result then add to map
                while (result.next()) {
                    Map<String, Object> deprecatedItemsDetails = new HashMap<String, Object>();
                    deprecatedItemsDetails.put("stock_id", result.getInt("stock_id"));
                    deprecatedItemsDetails.put("quantity", result.getFloat("quantity"));
                    deprecatedItemsDetails.put("stock_product_type_id", result.getInt("stock_product_type_id"));
                    deprecatedItemsDetails.put("expiry_date", result.getString("expiry_date"));
                    deprecatedItems.put(String.valueOf(result.getInt("id")), deprecatedItemsDetails);
                }
            }
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
        }

        return deprecatedItems;
    }

    // Gets the total reduced from supplied
    public double getTotalReducedOfSPE(int stock_product_expenses_id) {
        double totalReduced = 0.0;
        try (Connection con = this.zavPMSDB.createConnection();
                PreparedStatement stmt = con.prepareStatement(
                        "SELECT SUM(quantity) AS reduced, stock_product_expenses_id FROM `zav-pms-db`.stock_product_reduction WHERE stock_product_expenses_id = ? GROUP BY stock_product_expenses_id")) {
            stmt.setInt(1, stock_product_expenses_id);
            // Execute SQL Query
            stmt.execute();

            ResultSet result = stmt.getResultSet();
            // Checking if there are any matches
            if (isNoResult(result)) {
                result.close();
            } else {
                result.next();
                totalReduced = result.getFloat("reduced");
                result.close();
            }
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
        }
        return totalReduced;
    }

    // Returns drink variant object
    public DrinkVariant getBeverageByID(int beverage_id) {
        DrinkVariant beverage = null;
        try (Connection con = this.zavPMSDB.createConnection();
                PreparedStatement stmt = con.prepareStatement(
                        "SELECT * FROM `zav-pms-db`.drink_product JOIN products_name ON drink_product.products_name_id = products_name.id WHERE drink_product.id = ?;")) {
            stmt.setInt(1, beverage_id);
            // Execute SQL Query
            stmt.execute();

            ResultSet result = stmt.getResultSet();
            // Checking if there are any matches
            if (isNoResult(result)) {
                result.close();
            } else {
                result.next();
                beverage = new DrinkVariant(result.getInt(1), result.getString("product_name"),
                        result.getInt("products_name_id"), result.getInt("size"), result.getDouble("price"),
                        result.getInt("available_count"), result.getInt("critical_level"),
                        result.getBoolean("isVoided"), result.getDouble("discounted_price"),
                        result.getInt("preferred_unit_id"));
            }
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
        }
        return beverage;
    }

    public Stock getStockByID(int stock_id) {
        Stock stock = null;
        try (Connection con = this.zavPMSDB.createConnection();
                PreparedStatement stmt = con.prepareStatement(
                        "SELECT * FROM `zav-pms-db`.stock WHERE id = ?;")) {
            stmt.setInt(1, stock_id);
            // Execute SQL Query
            stmt.execute();

            ResultSet result = stmt.getResultSet();
            // Checking if there are any matches
            if (isNoResult(result)) {
                result.close();
            } else {
                result.next();
                stock = new Stock(result.getInt("id"), result.getString("stock_name"), result.getDouble("quantity"),
                        result.getInt("unit_measure_id"), result.getInt("stock_type_id"),
                        result.getInt("critical_level"), result.getBoolean("isVoided"));
            }
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
        }
        return stock;
    }

    public boolean createOrder(ObservableList<Order> orders, User loggedInUser) {
        try (Connection con = this.zavPMSDB.createConnection()) {
            for (@SuppressWarnings("unused")
            Order order : orders) {
                try (PreparedStatement stmt = con.prepareStatement(
                        "INSERT INTO orders (user_id, product_name, quantity, total_price, order_date) VALUES (?, ?, ?, ?, ?)")) {
                    stmt.setInt(1, loggedInUser.getId());
                    // stmt.setString(2, order.getProductName());
                    // stmt.setInt(3, order.getQuantity());
                    // stmt.setDouble(4, order.getTotalPrice());
                    stmt.setString(5, DateHelper.getCurrentDateTimeString());
                    stmt.execute();
                }
            }
            logAction(loggedInUser.getId(), loggedInUser.getUname(),
                    UserLogActions.Actions.CREATED_ORDER.getValue(),
                    DateHelper.getCurrentDateTimeString(), "created orders");
            return true;
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
        }
        return false;
    }

    // Returns inventory items in critical level
    public ObservableList<DeprecatedItem> getItemsInCriticalLevel() {
        String[] queries = {
                "SELECT * FROM `zav-pms-db`.drink_product WHERE isVoided = 0 AND critical_level >= available_count;",
                "SELECT * FROM `zav-pms-db`.stock WHERE isVoided = 0 AND critical_level >= quantity;" };
        ObservableList<DeprecatedItem> criticalLevelItems = FXCollections.observableArrayList();

        for (String query : queries) {
            try (Connection con = this.zavPMSDB.createConnection();
                    PreparedStatement stmt = con.prepareStatement(query)) {
                // Execute SQL Query
                stmt.execute();

                ResultSet result = stmt.getResultSet();
                // Checking if there are any matches
                if (isNoResult(result)) {
                    result.close();
                } else {
                    while (result.next()) {
                        // If first query
                        if (query.compareTo(queries[0]) == 0)
                            criticalLevelItems.add(new DeprecatedItem(getProductName(result.getInt("products_name_id")),
                                    result.getInt("available_count"), null,
                                    result.getInt("critical_level")));
                        else if (query.compareTo(queries[1]) == 0)
                            criticalLevelItems.add(new DeprecatedItem(result.getString("stock_name"),
                                    result.getInt("quantity"), null,
                                    result.getInt("critical_level")));
                    }
                    result.close();
                }
            } catch (Exception e) {
                PopupDialog.showErrorDialog(e, this.getClass().getName());
            }
        }
        return criticalLevelItems;
    }

}
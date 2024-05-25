package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import models.AdminHomePageModel;

import java.util.Map;

public class AdminHomePageController extends ParentController {
    AdminHomePageModel model;

    @FXML
    private Button orderButton;

    @FXML
    private Button transactionsButton;

    @FXML
    private Button inventoryButton;

    @FXML
    private Button reportsButton;

    @FXML
    private Button manageaccountButton;

    @FXML
    private Button maintenanceButton;

    @FXML
    private Button helpButton;

    @FXML
    private Button aboutButton;

    @FXML
    private Label usersname;

    @FXML
    private Label usersname1;

    @FXML
    private void initialize() {
        System.out.println("Initializing AdminHomePageController with user info: " + this.loggedInUserInfo); // Debugging statement
        if (this.loggedInUserInfo != null && this.loggedInUserInfo.containsKey("username")) {
            String username = this.loggedInUserInfo.get("username");
            System.out.println("Initializing with username: " + username); // Debugging statement
            usersname.setText(username);
            usersname1.setText(username);
        } else {
            System.out.println("User info is not set or does not contain 'username' key."); // Debugging statement
        }
    }

    // Action methods for buttons
    @FXML
    private void orderAction() {
        // action for the orderButton
    }
}

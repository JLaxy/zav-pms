package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import models.LoginModel;

public class LoginController extends ParentController {

    @FXML
    private TextField unameField, passField;
    @FXML
    private Button loginButton;
    @FXML
    private Label errorLabel;

    private final String INVALID_COMBINATION_MSG = "Invalid username and password combination!";
    private final String COOLDOWN_MSG = "You are not allowed to log-in until: ";
    private LoginModel model;

    @FXML
    public void initialize() {
        // Creating Model
        this.model = new LoginModel(this);
        // Checking for cooldown
        checkCooldown();
    }

    // Go Back Button Action
    public void backAction(ActionEvent e) {
        try {
            // Calling Root Switcher to go back to previous page.
            rootSwitcher.goBack();
        } catch (Exception ex) {
            System.out.println("Error at: " + getClass());
            ex.printStackTrace();
        }
    }

    // Login Button Action
    public void loginAction(ActionEvent e) {
        try {
            // Retrieving user information from model
            String[] userInfo = this.model.getUserInfo(unameField.getText(), passField.getText());
            if (userInfo[0] != "") {
                System.out.println("exists: " + userInfo[0] + ", " + userInfo[1]);
                // Hiding error label
                errorLabel.setVisible(false);
            } else {
                System.out.println("does not exists");
                // Showing error label
                errorLabel.setVisible(true);
            }

        } catch (Exception ex) {
            System.out.println("Error at: " + getClass());
            ex.printStackTrace();
        }
    }

    // Checks if cooldown is active
    private void checkCooldown() {
        // If cooldown is active
        if (this.model.isCooldownActive()) {
            // Disabling fields and button
            unameField.setDisable(true);
            passField.setDisable(true);
            loginButton.setDisable(true);
            errorLabel.setText(COOLDOWN_MSG);
            return;
        }
        // else, Configuring Error Label
        errorLabel.setText(INVALID_COMBINATION_MSG);
        errorLabel.setVisible(false);
    }
}
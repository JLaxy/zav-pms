package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class LoginController extends ParentController {

    @FXML
    private TextField unameField, passField;
    @FXML
    private Button loginButton;

    // Go Back Button Action
    public void goBack(ActionEvent e) {
        try {
            // Calling Root Switcher to go back to previous page.
            rootSwitcher.goBack();
        } catch (Exception ex) {
            System.out.println("Error at: " + getClass());
            ex.printStackTrace();
        }
    }

    // Login Button Action
    public void login(ActionEvent e) {
        try {
            // Checking if (account exists) / (query has no result)
            if (this.zavPMSDB.query.userLogin(unameField.getText(), passField.getText()))
                System.out.println("exists");
            else
                System.out.println("does not exists");

        } catch (Exception ex) {
            System.out.println("Error at: " + getClass());
            ex.printStackTrace();
        }
    }
}

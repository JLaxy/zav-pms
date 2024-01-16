package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import models.helpers.database.DBQuery;

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
            if (DBQuery.isNoResult(this.zavPMSDB.query.userLogin(unameField.getText(), passField.getText())))
                System.out.println("does not exist");
            else
                System.out.println("exists");

        } catch (Exception ex) {
            System.out.println("Error at: " + getClass());
            ex.printStackTrace();
        }
    }
}

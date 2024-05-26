package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class AdminHomePageController extends ParentController {
    @FXML
    private Button orderButton, transactionsButton, inventoryButton, reportsButton, manageAccountButton,
            maintenanceButton, helpButton, aboutButton;

    @FXML
    private Label welcomeNameLabel;

    // Syncs screen elements with passed user info
    public void configureScreen() {
        System.out.println("Initializing AdminHomePageController with user info: " + this.loggedInUserInfo);
        // Checks if valid user info
        if (this.loggedInUserInfo != null && this.loggedInUserInfo.containsKey("uname")) {
            String uname = this.loggedInUserInfo.get("uname");
            String fname = this.loggedInUserInfo.get("fname").toUpperCase();

            System.out.println("Initializing with username: " + uname); // Debugging statement

            welcomeNameLabel.setText("WELCOME, " + fname);
        } else {
            System.out.println("User info is not set or does not contain 'uname' key."); // Debugging statement
        }
    }

    // Action methods for buttons
    @FXML
    private void orderAction(ActionEvent e) {
        if ((Button) e.getSource() == this.orderButton) {
            initializeNextScreen_BP("../views/fxmls/OrderView.fxml", this.loggedInUserInfo, "ORDER");
        } else if ((Button) e.getSource() == this.transactionsButton) {
            System.out.println("transactions button");
            showReferences();
        } else if ((Button) e.getSource() == this.inventoryButton) {
            System.out.println("inventoryButton");
        } else if ((Button) e.getSource() == this.reportsButton) {
            System.out.println("reportsButton");
        } else if ((Button) e.getSource() == this.manageAccountButton) {
            System.out.println("manageAccountButton");
        } else if ((Button) e.getSource() == this.maintenanceButton) {
            System.out.println("maintenanceButton");
        } else if ((Button) e.getSource() == this.helpButton) {
            System.out.println("helpButton");
        } else if ((Button) e.getSource() == this.aboutButton) {
            System.out.println("aboutButton");
        }
    }
}

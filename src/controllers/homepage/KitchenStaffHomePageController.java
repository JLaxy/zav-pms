package controllers.homepage;

import controllers.ParentController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class KitchenStaffHomePageController extends ParentController {

    @FXML
    private Button orderButton, inventoryButton, helpButton, aboutButton;

    @FXML
    private Label welcomeNameLabel, unameLabel;

    // Syncs screen elements with passed user info
    public void configureScreen() {
        System.out.println("Initializing KitchenHomePage with user info: " + this.loggedInUserInfo);
        // Checks if valid user info
        if (this.loggedInUserInfo != null && this.loggedInUserInfo.containsKey("uname")) {
            String uname = this.loggedInUserInfo.get("uname");
            String fname = this.loggedInUserInfo.get("fname").toUpperCase();

            System.out.println("Initializing with username: " + uname); // Debugging statement

            welcomeNameLabel.setText("WELCOME, " + fname);
            unameLabel.setText(uname);
        } else {
            System.out.println("User info is not set or does not contain 'uname' key."); // Debugging statement
        }
    }

    // Action methods for buttons
    @FXML
    private void orderAction(ActionEvent e) {
        if ((Button) e.getSource() == this.orderButton) {
            System.out.println("order button");
        } else if ((Button) e.getSource() == this.inventoryButton) {
            System.out.println("inventoryButton");
        } else if ((Button) e.getSource() == this.helpButton) {
            System.out.println("helpButton");
        } else if ((Button) e.getSource() == this.aboutButton) {
            System.out.println("aboutButton");
        }
    }

    // Logout function
    @FXML
    private void logout(ActionEvent e) {

    }
}

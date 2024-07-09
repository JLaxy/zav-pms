package controllers.homepage;

import controllers.ParentController;
import enums.ScreenPaths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class CashierHomePageController extends ParentController {

    @FXML
    private Button orderButton, transactionsButton, helpButton, aboutButton;

    @FXML
    private Label welcomeNameLabel;

    // Syncs screen elements with passed user info
    public void configureScreen() {
        System.out.println("Initializing CashierHomePage with user info: " + this.loggedInUserInfo);
        // Checks if valid user info
        if (this.loggedInUserInfo != null && this.loggedInUserInfo.getUname() != null) {
            String uname = this.loggedInUserInfo.getUname();
            String fname = this.loggedInUserInfo.getFName().toUpperCase();

            System.out.println("Initializing with username: " + uname + " LINE 25"); // Debugging statement

            welcomeNameLabel.setText("WELCOME, " + fname);
        } else {
            System.out.println("User info is not set or does not contain 'uname' key."); // Debugging statement
        }
    }

    // Action methods for buttons
    @FXML
    private void orderAction(ActionEvent e) {
        if ((Button) e.getSource() == this.orderButton) {
            this.initializeNextScreen_BP(ScreenPaths.Paths.ORDER.getPath(), this.loggedInUserInfo, "ORDER");
        } else if ((Button) e.getSource() == this.transactionsButton) {
            this.initializeNextScreen_BP(ScreenPaths.Paths.TRANSACTION.getPath(), this.loggedInUserInfo,
                    "TRANSACTIONS");
        } else if ((Button) e.getSource() == this.helpButton) {
            this.initializeNextScreen_BP(ScreenPaths.Paths.HELP.getPath(), this.loggedInUserInfo,
                    "HELP");
        } else if ((Button) e.getSource() == this.aboutButton) {
            this.initializeNextScreen_BP(ScreenPaths.Paths.ABOUT.getPath(), this.loggedInUserInfo,
                    "ABOUT");
        }
    }
}

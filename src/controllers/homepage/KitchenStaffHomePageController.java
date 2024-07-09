package controllers.homepage;

import controllers.ParentController;
import controllers.inventory.InventoryController;
import enums.ScreenPaths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import models.helpers.JSONManager;

public class KitchenStaffHomePageController extends ParentController {

    @FXML
    private Button orderButton, inventoryButton, helpButton, aboutButton;

    @FXML
    private Label welcomeNameLabel;

    // Syncs screen elements with passed user info
    public void configureScreen() {
        System.out.println("Initializing KitchenHomePage with user info: " + this.loggedInUserInfo);
        // Checks if valid user info
        if (this.loggedInUserInfo != null && this.loggedInUserInfo.getUname() != null) {
            String uname = this.loggedInUserInfo.getUname();
            String fname = this.loggedInUserInfo.getFName().toUpperCase();

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
            this.initializeNextScreen_BP(ScreenPaths.Paths.ORDER.getPath(), this.loggedInUserInfo, "ORDER");
        } else if ((Button) e.getSource() == this.inventoryButton) {
            InventoryController controller = (InventoryController) this.initializeNextScreen_BP(
                    ScreenPaths.Paths.INVENTORY.getPath(), this.loggedInUserInfo,
                    "INVENTORY");

            // If setting is autoCheckExpiredItems is ON
            if (new JSONManager().getSetting("autoCheckExpiredItems").compareTo("true") == 0)
                controller.checkExpiredItems();
        } else if ((Button) e.getSource() == this.helpButton) {
            this.initializeNextScreen_BP(ScreenPaths.Paths.HELP.getPath(), this.loggedInUserInfo,
                    "HELP");
        } else if ((Button) e.getSource() == this.aboutButton) {
            this.initializeNextScreen_BP(ScreenPaths.Paths.ABOUT.getPath(), this.loggedInUserInfo,
                    "ABOUT");
            System.out.println("aboutButton");
        }
    }

    // Logout function
    @FXML
    private void logout(ActionEvent e) {

    }
}

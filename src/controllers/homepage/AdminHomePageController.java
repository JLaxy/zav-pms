package controllers.homepage;

import controllers.ParentController;
import controllers.inventory.InventoryController;
import controllers.manageaccounts.ManageAccountsController;
import enums.ScreenPaths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import models.helpers.JSONManager;

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
        } else if ((Button) e.getSource() == this.transactionsButton) {
            this.initializeNextScreen_BP(ScreenPaths.Paths.TRANSACTION.getPath(), this.loggedInUserInfo,
                    "TRANSACTIONS");
        } else if ((Button) e.getSource() == this.inventoryButton) {
            InventoryController controller = (InventoryController) this.initializeNextScreen_BP(
                    ScreenPaths.Paths.INVENTORY.getPath(), this.loggedInUserInfo,
                    "INVENTORY");

            // If setting is autoCheckExpiredItems is ON
            if (new JSONManager().getSetting("autoCheckExpiredItems").compareTo("true") == 0)
                controller.checkExpiredItems();

        } else if ((Button) e.getSource() == this.reportsButton) {
            this.initializeNextScreen_BP(ScreenPaths.Paths.REPORT.getPath(), this.loggedInUserInfo, "REPORT");
            System.out.println("reportsButton");
        } else if ((Button) e.getSource() == this.manageAccountButton) {
            ManageAccountsController controller = (ManageAccountsController) this.initializeNextScreen_BP(
                    ScreenPaths.Paths.MANAGE_ACCOUNTS.getPath(),
                    this.loggedInUserInfo,
                    "MANAGE ACCOUNTS");
            controller.syncUserTableView();
            System.out.println("manageAccountButton");
        } else if ((Button) e.getSource() == this.maintenanceButton) {
            this.initializeNextScreen_BP(ScreenPaths.Paths.MAINTENANCE.getPath(), this.loggedInUserInfo,
                    "MAINTENANCE");
            System.out.println("maintenanceButton");
        } else if ((Button) e.getSource() == this.helpButton) {
            System.out.println("helpButton");
        } else if ((Button) e.getSource() == this.aboutButton) {
            System.out.println("aboutButton");
        }
    }
}

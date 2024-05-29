package controllers.homepage;

import controllers.ParentController;
import controllers.manageaccounts.ManageAccountsController;
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
        showReferences();
    }

    // Action methods for buttons
    @FXML
    private void orderAction(ActionEvent e) {
        if ((Button) e.getSource() == this.orderButton) {
            this.initializeNextScreen_BP("../../views/fxmls/order/OrderView.fxml", this.loggedInUserInfo, "ORDER");
        } else if ((Button) e.getSource() == this.transactionsButton) {
            this.initializeNextScreen_BP("../../views/fxmls/transactions/TransactionsView.fxml", this.loggedInUserInfo,
                    "TRANSACTIONS");
        } else if ((Button) e.getSource() == this.inventoryButton) {
            this.initializeNextScreen_BP("../../views/fxmls/inventory/InventoryView.fxml", this.loggedInUserInfo,
                    "INVENTORY");
        } else if ((Button) e.getSource() == this.reportsButton) {
            this.initializeNextScreen_BP("../../views/fxmls/report/ReportView.fxml", this.loggedInUserInfo, "REPORT");
            System.out.println("reportsButton");
        } else if ((Button) e.getSource() == this.manageAccountButton) {
            ManageAccountsController controller = (ManageAccountsController) this.initializeNextScreen_BP(
                    "../../views/fxmls/manageaccounts/ManageAccountsView.fxml",
                    this.loggedInUserInfo,
                    "MANAGE ACCOUNTS");
            controller.syncUserTableView();
            System.out.println("manageAccountButton");
        } else if ((Button) e.getSource() == this.maintenanceButton) {
            this.initializeNextScreen_BP("../../views/fxmls/maintenance/MaintenanceView.fxml", this.loggedInUserInfo,
                    "MAINTENANCE");
            System.out.println("maintenanceButton");
        } else if ((Button) e.getSource() == this.helpButton) {
            System.out.println("helpButton");
        } else if ((Button) e.getSource() == this.aboutButton) {
            System.out.println("aboutButton");
        }
    }
}

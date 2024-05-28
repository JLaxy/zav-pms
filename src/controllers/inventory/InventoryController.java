package controllers.inventory;

import controllers.ParentController;
import javafx.fxml.FXML;

public class InventoryController extends ParentController {
    @FXML
    private void productInventory() {
        initializeNextScreen_BP("../../views/fxmls/inventory/ProductInventoryView.fxml", this.loggedInUserInfo, "PRODUCT INVENTORY");
        System.out.println("productInventory");
    }

    @FXML
    private void stockInventory() {
        initializeNextScreen_BP("../../views/fxmls/inventory/StockInventoryView.fxml", this.loggedInUserInfo, "STOCK INVENTORY");
        System.out.println("stockInventory");
    }

    @FXML
    private void goBack() {
        System.out.println("going back...");
        this.borderPaneRootSwitcher.goBack_BP();
    }

    @FXML
    private void viewrefillhistory() {
        System.out.println("View Refill History");
    }

    @FXML
    private void expiringitems() {
        System.out.println("Expiring Items");
    }

    @FXML
    private void itemsincriticallevel() {
        System.out.println("Items in Critical Level");
    }
}

package controllers.inventory;

import controllers.ParentController;
import enums.ScreenPaths;
import javafx.fxml.FXML;

public class InventoryController extends ParentController {
    @FXML
    private void productInventory() {
        initializeNextScreen_BP(ScreenPaths.Paths.PRODUCT_INVENTORY.getPath(), this.loggedInUserInfo,
                "PRODUCT INVENTORY");
        System.out.println("productInventory");
    }

    @FXML
    private void stockInventory() {
        initializeNextScreen_BP(ScreenPaths.Paths.STOCK_INVENTORY.getPath(), this.loggedInUserInfo,
                "STOCK INVENTORY");
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

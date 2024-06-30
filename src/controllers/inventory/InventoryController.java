package controllers.inventory;

import controllers.ParentController;
import enums.ScreenPaths;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import models.helpers.PopupDialog;
import models.inventory.InventoryModel;

public class InventoryController extends ParentController {

    private InventoryModel model;

    @FXML
    public void initialize() {
        System.out.println("REMEMBER TO CALL INITIALIZE SEPARATELY!");
        this.model = new InventoryModel(this);
    }

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
        InventoryRefillHistoryController controller = (InventoryRefillHistoryController) this.initializeNextScreen_BP(
                ScreenPaths.Paths.INVENTORY_REFILL_HISTORY.getPath(), loggedInUserInfo,
                "INVENTORY REFILL HISTORY");
        controller.retrieveInventoryHistory(null);
    }

    @FXML
    private void expiringitems() {
        ViewExpiringItemsController controller = (ViewExpiringItemsController) this
                .initializePopUpDialog(ScreenPaths.Paths.EXPIRING_ITEMS.getPath(), loggedInUserInfo);
        controller.retrieveExpiringItems();
    }

    @FXML
    private void itemsincriticallevel() {
        ViewCriticalLevelsController controller = (ViewCriticalLevelsController) this
                .initializePopUpDialog(ScreenPaths.Paths.CRITICAL_ITEMS.getPath(), loggedInUserInfo);
        controller.retrieveCriticalLevelItems();
    }

}

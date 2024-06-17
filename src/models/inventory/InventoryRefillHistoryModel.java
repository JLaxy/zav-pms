package models.inventory;

import controllers.inventory.InventoryRefillHistoryController;
import javafx.collections.ObservableList;
import models.schemas.PurchasedInventoryItem;

public class InventoryRefillHistoryModel {
    private InventoryRefillHistoryController controller;

    public InventoryRefillHistoryModel(InventoryRefillHistoryController controller) {
        this.controller = controller;
    }

    // Returns list of purchased inventory item
    public ObservableList<PurchasedInventoryItem> getInventoryPurchaseHistory(String inventoryItem) {
        return this.controller.getDBManager().query.getInventoryPurchaseHistory(inventoryItem);
    }
}

package models.inventory;

import java.util.Map;

import controllers.inventory.InventoryController;

public class InventoryModel {
    private InventoryController controller;

    public InventoryModel(InventoryController controller) {
        this.controller = controller;
    }

    // Returns true if there are deprecated items in the inventory
    public boolean hasExpiringItems() {
        Map<String, Object> deprecatedItems = this.controller.getDBManager().query.getDeprecatedItems("EXPIRED");

        return deprecatedItems.size() > 1;
    }
}

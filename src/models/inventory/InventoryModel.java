package models.inventory;

import controllers.inventory.InventoryController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.schemas.DeprecatedItem;

public class InventoryModel {
    private InventoryController controller;

    public InventoryModel(InventoryController controller) {
        this.controller = controller;
    }

    // Returns true if there are deprecated items in the inventory
    public boolean hasExpiringItems() {
        ObservableList<DeprecatedItem> deprecatedItems = FXCollections.observableArrayList();

        deprecatedItems.addAll(this.controller.getDBManager().query.getExpiredBeverages());
        deprecatedItems.addAll(this.controller.getDBManager().query.getExpiringBeverages());
        deprecatedItems.addAll(this.controller.getDBManager().query.getExpiredStock());
        deprecatedItems.addAll(this.controller.getDBManager().query.getExpiringStock());

        return deprecatedItems.size() > 0;
    }
}

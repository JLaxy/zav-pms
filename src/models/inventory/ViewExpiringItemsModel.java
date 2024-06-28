package models.inventory;

import controllers.inventory.ViewExpiringItemsController;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import models.schemas.ExpiringItems;

public class ViewExpiringItemsModel {

    private ViewExpiringItemsController controller;

    public ViewExpiringItemsModel(ViewExpiringItemsController controller) {
        this.controller = controller;
    }

    public ObservableList<ExpiringItems> getExpiringBeverages() {
        return this.controller.getDBManager().query.getExpiringBeverages();
    }

    public ObservableList<ExpiringItems> getExpiredBeverages() {
        return this.controller.getDBManager().query.getExpiredBeverages();
    }

    public ObservableList<ExpiringItems> getExpiringStock() {
        return this.controller.getDBManager().query.getExpiringStock();
    }

    public ObservableList<ExpiringItems> getExpiredStock() {
        return this.controller.getDBManager().query.getExpiredStock();
    }

}

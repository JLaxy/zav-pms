package models.inventory;

import controllers.inventory.ViewBeverageProductController;
import javafx.collections.ObservableList;
import models.schemas.DrinkVariant;

public class ViewBeverageProductModel {
    private ViewBeverageProductController controller;

    public ViewBeverageProductModel(ViewBeverageProductController controller) {
        this.controller = controller;
    }

    public ObservableList<DrinkVariant> getBeverageProducts(String beverage) {
        return this.controller.getDBManager().query.getBeverageProducts(beverage);
    }

}

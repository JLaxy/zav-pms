package models.inventory;

import controllers.inventory.ViewFoodProductController;
import javafx.collections.ObservableList;
import models.schemas.FoodVariant;

public class ViewFoodProductModel {
    private ViewFoodProductController controller;

    public ViewFoodProductModel(ViewFoodProductController controller) {
        this.controller = controller;
    }

    public ObservableList<FoodVariant> getFoodProducts(String foodItem) {
        return this.controller.getDBManager().query.getFoodProducts(foodItem);
    }
}

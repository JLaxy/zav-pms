package models.order;

import controllers.order.CreateOrderController;
import javafx.collections.ObservableList;
import models.schemas.DrinkVariant;
import models.schemas.FoodVariant;

public class CreateOrderModel {

    private CreateOrderController controller;

    public CreateOrderModel(CreateOrderController controller) {
        this.controller = controller;
    }

    public ObservableList<FoodVariant> getFoodProducts(String foodItem) {
        if (foodItem == null || foodItem.isEmpty()) {
            return this.controller.getDBManager().query.getFoodProducts(null);
        }
        return this.controller.getDBManager().query.getFoodProducts(foodItem);
    }

    public ObservableList<DrinkVariant> getDrinkProducts(String beverage) {
        if (beverage == null || beverage.isEmpty()) {
            return this.controller.getDBManager().query.getBeverageProducts(null);
        }
        return this.controller.getDBManager().query.getBeverageProducts(beverage);
    }

}

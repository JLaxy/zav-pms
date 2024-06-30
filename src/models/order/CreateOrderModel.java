package models.order;

import controllers.order.CreateOrderController;
import enums.StockProductType;
import javafx.collections.ObservableList;
import models.schemas.DrinkVariant;
import models.schemas.FoodVariant;
import models.schemas.Stock;

public class CreateOrderModel {

    private CreateOrderController controller;

    public CreateOrderModel(CreateOrderController controller) {
        this.controller = controller;
    }

    public int getProductId(String productName) {
        int productId = this.controller.getDBManager().query.getProductNameId(productName);
        System.out.println("getProductId for " + productName + " returned: " + productId); // Debugging statement
        return productId;
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

    public ObservableList<String> getDrinkSizes(int productId) {
        return this.controller.getDBManager().query.getDrinkSizes(productId);
    }

    public ObservableList<String> getFoodSizes(int productId) {
        return this.controller.getDBManager().query.getFoodSizes(productId);
    }

    public StockProductType.Type getProductType(int productId) {
        return this.controller.getDBManager().query.getProductType(productId);
    }

    public FoodVariant getFoodVariantBySize(int productsNameId, String size) {
        return this.controller.getDBManager().query.getFoodVariantBySize(productsNameId, size);
    }

    public ObservableList<Stock> getStockRequirements(FoodVariant foodVariant) {
        return this.controller.getDBManager().query.getStockRequirements(foodVariant);
    }
}

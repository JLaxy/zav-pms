package models.inventory;

import controllers.inventory.RegisterNewFoodVariantDetailsController;
import enums.StockProductType;
import enums.DatabaseLists.Lists;
import javafx.collections.ObservableList;
import models.schemas.FoodVariant;
import models.schemas.Stock;
import models.schemas.User;

public class RegisterNewFoodVariantDetailsModel {
    private RegisterNewFoodVariantDetailsController controller;

    public RegisterNewFoodVariantDetailsModel(RegisterNewFoodVariantDetailsController controller) {
        this.controller = controller;
    }

    public ObservableList<String> getServingSizes() {
        return this.controller.getDBManager().query.getListOnDatabase(Lists.PRODUCT_SERVING_SIZE);
    }

    public boolean registerNewFood(String productName, User loggedInUser) {
        return this.controller.getDBManager().query.registerNewProduct(loggedInUser, productName,
                StockProductType.Type.FOOD);
    }

    public boolean registerNewFoodVariant(User loggedInUnewUser, FoodVariant newProduct, String productName) {
        return this.controller.getDBManager().query.registerNewFoodVariant(loggedInUnewUser, newProduct, productName);
    }

    public boolean registerStockRequired(User loggedInUser, ObservableList<Stock> requiredStocks,
            String productName, int servingSizeID) {
        return this.controller.getDBManager().query.registerRequiredStocks(loggedInUser, requiredStocks,
                this.controller.getDBManager().query.getFoodVariant(
                        this.controller.getDBManager().query.getProductNameId(productName), servingSizeID));
    }

    public int getProductNameId(String productName) {
        return this.controller.getDBManager().query.getProductNameId(productName);
    }
}

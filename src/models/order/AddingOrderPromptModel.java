package models.order;

import controllers.order.AddingOrderPromptController;
import enums.StockProductType;
import javafx.collections.ObservableList;
import models.helpers.PopupDialog;
import models.schemas.DrinkVariant;
import models.schemas.FoodVariant;
import models.schemas.Stock;
import models.helpers.database.DBQuery;

public class AddingOrderPromptModel {

    private AddingOrderPromptController controller;
    private DBQuery dbQuery;

    public AddingOrderPromptModel(AddingOrderPromptController controller) {
        this.controller = controller;
        this.dbQuery = controller.getDBManager().query;
    }

    public double fetchAmountFromDatabase(String productName, String selectedSize, int quantity) {
        int productId = dbQuery.getProductNameId(productName);
        StockProductType.Type productType = dbQuery.getProductType(productId);

        if (productType == StockProductType.Type.BEVERAGE) {
            DrinkVariant drinkVariant = dbQuery.getDrinkVariantBySize(productId, selectedSize);
            if (drinkVariant != null) {
                return drinkVariant.getPrice() * quantity;
            } else {
                PopupDialog.showCustomErrorDialog("Error: Drink variant not found.");
                return 0.0;
            }
        } else if (productType == StockProductType.Type.FOOD) {
            FoodVariant foodVariant = dbQuery.getFoodVariantBySize(productId, selectedSize);
            if (foodVariant != null) {
                return foodVariant.getRegular_price() * quantity;
            } else {
                PopupDialog.showCustomErrorDialog("Error: Food variant not found.");
                return 0.0;
            }
        }
        return 0.0; // Default fallback, should not reach here ideally
    }

    public double fetchDiscountedPriceFromDatabase(String productName, String selectedSize, int quantity) {
        int productId = dbQuery.getProductNameId(productName);
        StockProductType.Type productType = dbQuery.getProductType(productId);

        if (productType == StockProductType.Type.BEVERAGE) {
            DrinkVariant drinkVariant = dbQuery.getDrinkVariantBySize(productId, selectedSize);
            if (drinkVariant != null) {
                return drinkVariant.getDiscounted_price() * quantity;
            } else {
                PopupDialog.showCustomErrorDialog("Error: Drink variant not found.");
                return 0.0;
            }
        } else if (productType == StockProductType.Type.FOOD) {
            FoodVariant foodVariant = dbQuery.getFoodVariantBySize(productId, selectedSize);
            if (foodVariant != null) {
                return foodVariant.getDiscounted_price() * quantity;
            } else {
                PopupDialog.showCustomErrorDialog("Error: Food variant not found.");
                return 0.0;
            }
        }
        return 0.0; // Default fallback, should not reach here ideally
    }

    public boolean checkOverallStockSufficiency(ObservableList<AddingOrderPromptController.StockRequirement> stockRequirements, int quantity) {
        for (AddingOrderPromptController.StockRequirement requirement : stockRequirements) {
            if ("Insufficient".equals(requirement.getStatus())) {
                return false;
            }
        }
        return true;
    }
}

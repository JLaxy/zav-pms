package models.inventory;

import java.time.LocalDate;

import controllers.inventory.IncreaseBeverageController;
import models.schemas.DrinkVariant;
import models.schemas.User;

public class IncreaseBeverageModel {
    private IncreaseBeverageController controller;

    public IncreaseBeverageModel(IncreaseBeverageController controller) {
        this.controller = controller;
    }

    public boolean logBeveragePurchase(int beverageId, int quantity, double totalCost, LocalDate datePurchased,
            LocalDate expiryDate, int stockProductTypeID, User loggedInUser, String stockName) {
        return this.controller.getDBManager().query.logStockProductPurchase(beverageId, quantity, totalCost,
                datePurchased, expiryDate, stockProductTypeID, loggedInUser, stockName);
    }

    public boolean incrementBeverage(DrinkVariant selectedProduct, int quantity, User loggedInUser) {
        DrinkVariant updatedBeverage = selectedProduct.getCopy();
        updatedBeverage.increaseQuantity(quantity);

        return this.controller.getDBManager().query.editBeverage(selectedProduct, updatedBeverage, loggedInUser,
                DrinkVariant.getChangesMessage(selectedProduct, updatedBeverage));
    }
}

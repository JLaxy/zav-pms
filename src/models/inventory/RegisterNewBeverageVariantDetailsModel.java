package models.inventory;

import controllers.inventory.RegisterNewBeverageVariantDetailsController;
import enums.StockProductType;
import models.schemas.DrinkVariant;
import models.schemas.User;

public class RegisterNewBeverageVariantDetailsModel {
    private RegisterNewBeverageVariantDetailsController controller;

    public RegisterNewBeverageVariantDetailsModel(RegisterNewBeverageVariantDetailsController controller) {
        this.controller = controller;
    }

    // Registers new beverage in system
    public boolean registerNewBeverage(User loggedInUser, String beverageName) {
        return this.controller.getDBManager().query.registerNewProduct(loggedInUser, beverageName,
                StockProductType.Type.BEVERAGE);
    }

    // Registers new drink variant in system
    public boolean registerNewBeverageVariant(User loggedInUser, DrinkVariant newDrinkVariant, String beverageName) {
        return this.controller.getDBManager().query.registerNewDrinkVariant(loggedInUser, newDrinkVariant,
                beverageName);
    }

    public int getProductNameId(String productName) {
        return this.controller.getDBManager().query.getProductNameId(productName);
    }
}

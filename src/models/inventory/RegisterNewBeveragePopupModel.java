package models.inventory;

import controllers.inventory.RegisterNewBeveragePopupController;
import enums.StockProductType;
import models.schemas.User;

public class RegisterNewBeveragePopupModel {
    private RegisterNewBeveragePopupController controller;

    public RegisterNewBeveragePopupModel(RegisterNewBeveragePopupController controller) {
        this.controller = controller;
    }

    // Returns true if sucessfully registered new beverage
    public boolean registerNewBeverage(User loggedInUser, String newBeverage) {
        return this.controller.getDBManager().query.registerNewProduct(loggedInUser, newBeverage,
                StockProductType.Type.BEVERAGE);
    }

    // Returns true if already exists
    public boolean doesProductNameAlreadyExist(String productName) {
        return this.controller.getDBManager().query.getProductNameId(productName) != -1;
    }
}

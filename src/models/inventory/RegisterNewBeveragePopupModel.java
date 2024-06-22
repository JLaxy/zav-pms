package models.inventory;

import controllers.inventory.RegisterNewBeveragePopupController;

public class RegisterNewBeveragePopupModel {
    private RegisterNewBeveragePopupController controller;

    public RegisterNewBeveragePopupModel(RegisterNewBeveragePopupController controller) {
        this.controller = controller;
    }

    // Returns true if already exists
    public boolean doesProductNameAlreadyExist(String productName) {
        return this.controller.getDBManager().query.getProductNameId(productName) != -1;
    }
}

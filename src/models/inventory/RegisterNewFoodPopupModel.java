package models.inventory;

import controllers.inventory.RegisterNewFoodPopupController;

public class RegisterNewFoodPopupModel {
    private RegisterNewFoodPopupController controller;

    public RegisterNewFoodPopupModel(RegisterNewFoodPopupController controller) {
        this.controller = controller;
    }

    public boolean doesFoodProductExist(String productName) {
        return this.controller.getDBManager().query.getProductNameId(productName) != -1;
    }
}

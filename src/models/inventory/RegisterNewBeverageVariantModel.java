package models.inventory;

import controllers.inventory.RegisterNewBeverageVariantController;
import javafx.collections.ObservableList;

public class RegisterNewBeverageVariantModel {
    private RegisterNewBeverageVariantController controller;

    public RegisterNewBeverageVariantModel(RegisterNewBeverageVariantController controller) {
        this.controller = controller;
    }

    // Returns list of beverages
    public ObservableList<String> getBeverageProducts() {
        return this.controller.getDBManager().query.getBeverageProducts();
    }
}

package models.inventory;

import controllers.inventory.RegisterNewFoodVariantController;
import javafx.collections.ObservableList;

public class RegisterNewFoodVariantModel {
    private RegisterNewFoodVariantController controller;

    public RegisterNewFoodVariantModel(RegisterNewFoodVariantController controller) {
        this.controller = controller;
    }

    public ObservableList<String> getFoodProducts() {
        return this.controller.getDBManager().query.getFoodProducts();
    }
}

package models.inventory;

import controllers.inventory.EditFoodPopupController;
import models.schemas.FoodVariant;
import models.schemas.User;

public class EditFoodPopupModel {

    private EditFoodPopupController controller;

    public EditFoodPopupModel(EditFoodPopupController controller) {
        this.controller = controller;
    }

    public boolean editFoodVariant(FoodVariant selectedFoodVariant, FoodVariant updatedFoodVariant, User loggedInUser) {
        return this.controller.getDBManager().query.editFoodVariant(selectedFoodVariant, updatedFoodVariant,
                loggedInUser, FoodVariant.getChangesMessages(selectedFoodVariant, updatedFoodVariant));
    }

}

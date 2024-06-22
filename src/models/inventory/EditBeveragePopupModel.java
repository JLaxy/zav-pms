package models.inventory;

import controllers.inventory.EditBeveragePopupController;
import models.schemas.DrinkVariant;
import models.schemas.User;

public class EditBeveragePopupModel {
    private EditBeveragePopupController controller;

    public EditBeveragePopupModel(EditBeveragePopupController controller) {
        this.controller = controller;
    }

    public boolean editBeverage(DrinkVariant oldBeverage, DrinkVariant newBeverage, User loggedInUser) {
        return this.controller.getDBManager().query.editBeverage(oldBeverage, newBeverage, loggedInUser,
                DrinkVariant.getChangesMessage(oldBeverage, newBeverage));
    }
}

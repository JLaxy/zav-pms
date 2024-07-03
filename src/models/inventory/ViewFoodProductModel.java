package models.inventory;

import controllers.inventory.ViewFoodProductController;
import enums.StockProductType;
import enums.UserLogActions;
import javafx.collections.ObservableList;
import models.helpers.DateHelper;
import models.helpers.PopupDialog;
import models.schemas.FoodVariant;
import models.schemas.PurchasedInventoryItem;
import models.schemas.User;

public class ViewFoodProductModel {
    private ViewFoodProductController controller;

    public ViewFoodProductModel(ViewFoodProductController controller) {
        this.controller = controller;
    }

    public ObservableList<FoodVariant> getFoodProducts(String foodItem) {
        return this.controller.getDBManager().query.getFoodProducts(foodItem);
    }

    public boolean confirmDecrease(FoodVariant selectedFood, String reductionType, int quantity, User loggedInUser) {
        FoodVariant updatedFood = selectedFood.getCopy();
        updatedFood.updateQuantity(updatedFood.getAvailable_count() - quantity);

        PurchasedInventoryItem record = new PurchasedInventoryItem(null, selectedFood.getId(),
                selectedFood.getAvailable_count(), null, null, StockProductType.Type.FOOD.getValue(), null,
                selectedFood.getFood_name(), null, null);

        int actionID = UserLogActions.Actions.REMOVED_EXPIRED_ITEM.getValue();

        if (reductionType.compareTo("mishandled") == 0)
            actionID = UserLogActions.Actions.REMOVED_MISHANDLED_ITEM.getValue();

        if (this.controller.getDBManager().query.manualStockProductReduction(record, quantity, reductionType)) {
            if (this.controller.getDBManager().query.editFoodVariant(selectedFood, updatedFood, loggedInUser,
                    FoodVariant.getChangesMessages(selectedFood, updatedFood))) {
                this.controller.getDBManager().query.logAction(loggedInUser.getId(), loggedInUser.getUname(), actionID,
                        DateHelper.getCurrentDateTimeString(),
                        "inventory item \"" + selectedFood.getFood_name() + " "
                                + selectedFood.getServing_size_id_string() + "\"");
                PopupDialog.showInfoDialog("Success", "Successfully removed Food Product from Database");
                return true;
            }
            PopupDialog.showCustomErrorDialog("Unable to reduce product quantity! Reduction was recorded.");
        }

        PopupDialog.showCustomErrorDialog("Unable to save to database! Aborting operation.");
        return false;
    }
}

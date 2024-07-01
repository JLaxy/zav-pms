package models.inventory;

import java.util.Map;

import controllers.inventory.SelectBeverageDecreaseController;
import enums.UserLogActions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.helpers.DateHelper;
import models.helpers.NumberHelper;
import models.helpers.PopupDialog;
import models.schemas.DrinkVariant;
import models.schemas.PurchasedInventoryItem;
import models.schemas.Stock;
import models.schemas.User;
import models.schemas.UserLog;

public class SelectBeverageDecreaseModel {
    private SelectBeverageDecreaseController controller;

    public SelectBeverageDecreaseModel(SelectBeverageDecreaseController controller) {
        this.controller = controller;
    }

    public boolean confirmDecrease(PurchasedInventoryItem purchasedInventoryItem, int decreaseQuantity,
            String reductionType, User loggedInUser) {

        DrinkVariant origDrink = this.controller.getDBManager().query
                .getBeverageByID(purchasedInventoryItem.getStock_id());
        DrinkVariant decreasedDrink = origDrink.getCopy();

        decreasedDrink.updateQuantity(decreasedDrink.getAvailable_count() - decreaseQuantity);

        if (reductionType.compareTo("used") == 0) {
            PopupDialog.showInfoDialog("TODO", "IMPLEMENT FUNCTION FOR USED");
            return true;
        }

        if (this.controller.getDBManager().query.manualStockProductReduction(purchasedInventoryItem, decreaseQuantity,
                reductionType)) {
            if (this.controller.getDBManager().query.editBeverage(origDrink, decreasedDrink, loggedInUser,
                    DrinkVariant.getChangesMessage(origDrink, decreasedDrink))) {
                int action_id = -1;
                if (reductionType.compareTo("expired") == 0) {
                    action_id = UserLogActions.Actions.REMOVED_EXPIRED_ITEM.getValue();
                } else if (reductionType.compareTo("mishandled") == 0) {
                    action_id = UserLogActions.Actions.REMOVED_MISHANDLED_ITEM.getValue();
                } else if (reductionType.compareTo("used") == 0) {
                    action_id = UserLogActions.Actions.USED_ITEM.getValue();
                }

                try {
                    this.controller.getDBManager().query.logAction(loggedInUser.getId(), loggedInUser.getUname(),
                            action_id, DateHelper.getCurrentDateTimeString(),
                            "inventory item \"" + purchasedInventoryItem.getInventory_item_name() + "\"");
                } catch (Exception e) {
                    PopupDialog.showCustomErrorDialog("Unable to log action in user logs");
                    return false;
                }
                return true;
            }
            PopupDialog.showCustomErrorDialog(
                    "Was not able to modify beverage quantity! Stock product reduction was recorded");
        }

        return false;
    }

    public ObservableList<PurchasedInventoryItem> retrieveItemPurchases(DrinkVariant drink) {
        ObservableList<PurchasedInventoryItem> itemPurchases = FXCollections.observableArrayList();
        Map<String, Object> retrievedItemPurchases = this.controller.getDBManager().query
                .getBeverageExpenses(drink.getId());

        for (Map.Entry<String, Object> itemPurchase : retrievedItemPurchases.entrySet()) {
            Map<String, Object> itemPurchaseDetails = (Map<String, Object>) itemPurchase.getValue();

            double quantityLeft = Double.valueOf(NumberHelper
                    .toTwoDecimalPlaces(Double.valueOf(String.valueOf(itemPurchaseDetails.get("quantity")))
                            - this.controller.getDBManager().query
                                    .getTotalReducedOfSPE(Integer.valueOf(itemPurchase.getKey()))));

            // If beverage is less than 1
            if (quantityLeft < 1)
                continue;

            itemPurchases.add(new PurchasedInventoryItem(Integer.valueOf(itemPurchase.getKey()),
                    Integer.valueOf(String.valueOf(itemPurchaseDetails.get("stock_id"))),
                    quantityLeft,
                    Double.valueOf(String.valueOf(itemPurchaseDetails.get("total_cost"))),
                    String.valueOf(itemPurchaseDetails.get("date_purchased")),
                    Integer.valueOf(String.valueOf(itemPurchaseDetails.get("stock_product_type_id"))),
                    String.valueOf(itemPurchaseDetails.get("expiry_date")),
                    drink.getProduct_name(),
                    "bottle", "0"));
        }

        return itemPurchases;
    }
}

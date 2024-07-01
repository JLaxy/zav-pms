package models.inventory;

import java.util.Map;

import controllers.inventory.SelectStockDecreaseController;
import enums.UserLogActions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.helpers.DateHelper;
import models.helpers.NumberHelper;
import models.helpers.PopupDialog;
import models.schemas.PurchasedInventoryItem;
import models.schemas.Stock;
import models.schemas.User;

public class SelectStockDecreaseModel {
    private SelectStockDecreaseController controller;

    public SelectStockDecreaseModel(SelectStockDecreaseController controller) {
        this.controller = controller;
    }

    public boolean confirmDecrease(PurchasedInventoryItem purchasedInventoryItem, double decreaseQuantity,
            String reductionType, User loggedInUser) {
        Stock origStock = this.controller.getDBManager().query.getStockByID(purchasedInventoryItem.getStock_id());
        Stock decreasedStock = origStock.getCopy();

        decreasedStock.updateQuantity(decreasedStock.getQuantity() - decreaseQuantity);

        // ADD STOCK_PRODUCT_REDUCTION LOG
        if (this.controller.getDBManager().query.manualStockProductReduction(purchasedInventoryItem, decreaseQuantity,
                reductionType)) {
            if (this.controller.getDBManager().query.editStock(decreasedStock, loggedInUser,
                    Stock.getChangesMessages(origStock, decreasedStock))) {
                int action_id = -1;
                if (reductionType.compareTo("expired") == 0) {
                    action_id = UserLogActions.Actions.REMOVED_EXPIRED_ITEM.getValue();
                } else if (reductionType.compareTo("mishandled") == 0) {
                    action_id = UserLogActions.Actions.REMOVED_MISHANDLED_ITEM.getValue();
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
                    "Was not able to modify stock quantity! Stock product reduction was recorded");
        }

        // MODIFY STOCK QUANTITY
        return false;
    }

    public ObservableList<PurchasedInventoryItem> getInventoryItemPurchases(int stockID) {
        ObservableList<PurchasedInventoryItem> itemPurchases = FXCollections.observableArrayList();

        Map<String, Object> retrievedStockPurchases = this.controller.getDBManager().query
                .getStockProductExpensesOfItem(stockID);

        for (Map.Entry<String, Object> purchasedInventoryItem : retrievedStockPurchases.entrySet()) {
            Map<String, Object> purchasedInventoryItemDetails = (Map<String, Object>) purchasedInventoryItem.getValue();

            double quantityLeft = Double.valueOf(NumberHelper
                    .toTwoDecimalPlaces(Double.valueOf(String.valueOf(purchasedInventoryItemDetails.get("quantity")))
                            - this.controller.getDBManager().query
                                    .getTotalReducedOfSPE(Integer.valueOf(purchasedInventoryItem.getKey()))));

            // If no more, then skip
            if (quantityLeft < 0.1)
                continue;

            Stock retrievedStock = this.controller.getDBManager().query
                    .getStockByID(
                            Integer.valueOf(String.valueOf(purchasedInventoryItemDetails.get("stock_id"))));

            itemPurchases.add(new PurchasedInventoryItem(Integer.valueOf(purchasedInventoryItem.getKey()),
                    Integer.valueOf(String.valueOf(purchasedInventoryItemDetails.get("stock_id"))),
                    quantityLeft,
                    Double.valueOf(String.valueOf(purchasedInventoryItemDetails.get("total_cost"))),
                    String.valueOf(purchasedInventoryItemDetails.get("date_purchased")),
                    Integer.valueOf(String.valueOf(purchasedInventoryItemDetails.get("stock_product_type_id"))),
                    String.valueOf(purchasedInventoryItemDetails.get("expiry_date")),
                    retrievedStock.getStock_name(),
                    retrievedStock.getUnit_measure_id_string(), "0"));
        }

        return itemPurchases;
    }
}

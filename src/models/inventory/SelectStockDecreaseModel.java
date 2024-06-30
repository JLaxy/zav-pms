package models.inventory;

import java.util.Map;

import controllers.inventory.SelectStockDecreaseController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.helpers.NumberHelper;
import models.schemas.PurchasedInventoryItem;
import models.schemas.Stock;

public class SelectStockDecreaseModel {
    private SelectStockDecreaseController controller;

    public SelectStockDecreaseModel(SelectStockDecreaseController controller) {
        this.controller = controller;
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

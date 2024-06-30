package models.inventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import controllers.inventory.ViewExpiringItemsController;
import enums.PreferredUnits;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.helpers.DateHelper;
import models.helpers.NumberHelper;
import models.schemas.DeprecatedItem;
import models.schemas.DrinkVariant;
import models.schemas.Stock;

public class ViewExpiringItemsModel {

    private ViewExpiringItemsController controller;

    public ViewExpiringItemsModel(ViewExpiringItemsController controller) {
        this.controller = controller;
    }

    // Returns list ObservableList of expiring items
    public ObservableList<DeprecatedItem> getDeprecatedItems(String deprecatedItemType) {
        ObservableList<DeprecatedItem> finalExpiringItemList = FXCollections.observableArrayList();

        // Getting all of the ID that are expiring in inventory
        Map<String, Object> expiringItems = this.controller.getDBManager().query.getDeprecatedItems(deprecatedItemType);

        try {
            // For each expiring item
            for (Map.Entry<String, Object> expiringItem : expiringItems.entrySet()) {
                // Retrieve details of expiring item
                HashMap<String, Object> expiringItemDetails = (HashMap<String, Object>) expiringItem.getValue();

                String inventory_item = "";

                // Calulating remaining items; stock_product_expenses_quantity -
                // stock_product_reduction_quantity
                double remainingQuantity = Double.valueOf(String.valueOf(expiringItemDetails.get("quantity")))
                        - this.controller.getDBManager().query
                                .getTotalReducedOfSPE(Integer.valueOf(expiringItem.getKey()));
                // No more remaining quantity
                if (remainingQuantity < 1)
                    continue;

                // if beverage
                if ((int) expiringItemDetails.get("stock_product_type_id") == 1) {
                    // Identifying beverage
                    DrinkVariant retrievedBeverage = this.controller.getDBManager().query
                            .getBeverageByID(Integer.parseInt(String.valueOf(expiringItemDetails.get("stock_id"))));

                    // Skip if is item is voided
                    if (retrievedBeverage.isVoided())
                        continue;

                    // Formatting name to Liters and ML respectively
                    if (retrievedBeverage.getPreferred_unit_id() == PreferredUnits.Units.LITERS.getValue())
                        inventory_item = retrievedBeverage.getProduct_name() + " "
                                + NumberHelper.mililiterToLiter(retrievedBeverage.getSize()) + "L";
                    else if (retrievedBeverage.getPreferred_unit_id() == PreferredUnits.Units.MILILITERS.getValue())
                        inventory_item = retrievedBeverage.getProduct_name() + " " + retrievedBeverage.getSize() + "mL";

                } else if ((int) expiringItemDetails.get("stock_product_type_id") == 2) {
                    Stock retrievedStock = this.controller.getDBManager().query
                            .getStockByID(Integer.parseInt(String.valueOf(expiringItemDetails.get("stock_id"))));

                    // Skip if is item is voided
                    if (retrievedStock.getisVoided())
                        continue;

                    // Retrieving name
                    inventory_item = retrievedStock.getStock_name() + " : "
                            + retrievedStock.getUnit_measure_id_string();
                }

                System.out.println("deprecated item: " + inventory_item);
                System.out.println("key: " + expiringItem.getKey());
                System.out.println(expiringItemDetails);
                System.out.println("total reduced: " + this.controller.getDBManager().query
                        .getTotalReducedOfSPE(Integer.valueOf(expiringItem.getKey())));
                System.out.println("remaining: " + remainingQuantity);
                System.out.println();

                finalExpiringItemList.add(new DeprecatedItem(inventory_item, remainingQuantity,
                        DateHelper.stringToDate(String.valueOf(expiringItemDetails.get("expiry_date"))), 0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Sort according to inventory name
        Comparator<DeprecatedItem> sorter = Comparator.comparing(DeprecatedItem::getInventory_item);
        Collections.sort(finalExpiringItemList, sorter);

        return finalExpiringItemList;
    }

}

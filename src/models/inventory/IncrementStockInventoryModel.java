package models.inventory;

import java.time.LocalDate;

import controllers.inventory.IncrementStockInventoryController;
import models.schemas.Stock;
import models.schemas.User;

public class IncrementStockInventoryModel {

    private IncrementStockInventoryController controller;

    public IncrementStockInventoryModel(IncrementStockInventoryController controller) {
        this.controller = controller;
    }

    public boolean logStockProductPurchase(Stock selectedStock, double quantity, double totalCost,
            LocalDate datePurchased,
            LocalDate expiryDate, int stockProductTypeID, User loggedInUser) {
        return this.controller.getDBManager().query.logStockProductPurchase(selectedStock.getId(), quantity, totalCost,
                datePurchased, expiryDate, stockProductTypeID, loggedInUser, selectedStock.getStock_name());
    }

    public boolean incrementStock(Stock selectedStock, double quantity, User loggedInUser) {
        // Create copy then increment
        Stock updatedStock = selectedStock.getCopy();
        updatedStock.incrementQuantity(quantity);

        return this.controller.getDBManager().query.editStock(updatedStock, loggedInUser,
                Stock.getChangesMessages(selectedStock, updatedStock));
    }
}

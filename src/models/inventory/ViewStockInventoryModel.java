package models.inventory;

import controllers.inventory.ViewStockInventoryController;
import javafx.collections.ObservableList;
import models.schemas.Stock;

public class ViewStockInventoryModel {
    private ViewStockInventoryController controller;

    public ViewStockInventoryModel(ViewStockInventoryController controller) {
        this.controller = controller;
    }

    public ObservableList<Stock> getStocks(String stockName) {
        return this.controller.getDBManager().query.getStocks(stockName);
    }
}

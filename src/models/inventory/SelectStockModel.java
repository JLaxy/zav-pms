package models.inventory;

import controllers.inventory.SelectStockController;
import javafx.collections.ObservableList;
import models.schemas.Stock;

public class SelectStockModel {
    private SelectStockController controller;

    public SelectStockModel(SelectStockController controller) {
        this.controller = controller;
    }

    public ObservableList<Stock> getStocks(String stockName) {
        return this.controller.getDBManager().query.getStocks(stockName);
    }
}

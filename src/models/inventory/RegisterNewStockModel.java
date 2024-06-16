package models.inventory;

import controllers.inventory.RegisterNewStockController;
import enums.DatabaseLists;
import javafx.collections.ObservableList;
import models.schemas.Stock;
import models.schemas.User;

public class RegisterNewStockModel {

    RegisterNewStockController controller;

    public RegisterNewStockModel(RegisterNewStockController controller) {
        this.controller = controller;
    }
    
    // Returns list of unit measure in database
    public ObservableList<String> getUnitMeasure() {
        return this.controller.getDBManager().query
        .getListOnDatabase(DatabaseLists.Lists.UNIT_MEASURE);
    }

    // Returns list of stock type in database
    public ObservableList<String> getStockType() {
        return this.controller.getDBManager().query.getListOnDatabase(DatabaseLists.Lists.STOCK_TYPE);
    }

    // Save new stock to database
    public boolean saveNewStock(Stock stock, User loggedInUser) {
        return this.controller.getDBManager().query.saveNewStock(stock, loggedInUser);
    }
}

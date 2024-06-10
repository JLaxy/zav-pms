package models.inventory;

import controllers.inventory.RegisterNewStockTypeController;
import models.schemas.StockType;
import models.schemas.User;

public class RegisterNewStockTypeModel {

    private RegisterNewStockTypeController controller;

    public RegisterNewStockTypeModel(RegisterNewStockTypeController controller) {
        this.controller = controller;
    }

    // Save new stock type to database
    public boolean saveNewStockType(StockType stockType, User loggedInUser) {
        return this.controller.getDBManager().query.saveNewStockType(stockType, loggedInUser);
    }

}

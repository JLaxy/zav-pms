package models.inventory;

import controllers.inventory.RegisterNewStockController;
import models.schemas.Stock;
import models.schemas.User;

public class RegisterNewStockModel {

    private RegisterNewStockController controller;

    public RegisterNewStockModel(RegisterNewStockController controller) {
        this.controller = controller;
    }

    // Save new stock to database
    public boolean saveNewStock(Stock stock, User loggedInUser) {
        return this.controller.getDBManager().query.saveNewStock(stock, loggedInUser);
    }

}

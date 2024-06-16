package models.inventory;

import controllers.inventory.EditStockInventoryController;
import models.schemas.Stock;
import models.schemas.User;

public class EditStockInventoryModel {

    private EditStockInventoryController controller;

    public EditStockInventoryModel(EditStockInventoryController controller) {
        this.controller = controller;
    }

    public boolean editStock(Stock updatedStock, Stock oldStock, User loggedInUser) {
        return this.controller.getDBManager().query.editStock(updatedStock, loggedInUser,
                Stock.getChangesMessages(oldStock, updatedStock));
    }

}

package models.transactions;

import controllers.transactions.AddPaymentController;
import models.helpers.database.DBManager;

import java.util.List;

public class AddPaymentModel {

    private AddPaymentController controller;

    public AddPaymentModel(AddPaymentController addPaymentController) {
        this.controller = addPaymentController;
    }

    public List<String> getPaymentTypes() {
        // Fetch payment types from the mode_of_payment table in the database
        DBManager dbManager = controller.getDBManager();
        return dbManager.query.getModeOfPaymentTypes();
    }
}

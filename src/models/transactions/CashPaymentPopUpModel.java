package models.transactions;

import java.util.List;

import controllers.transactions.AddPaymentController;
import controllers.transactions.CashPaymentPopUpController;
import models.helpers.database.DBManager;

public class CashPaymentPopUpModel {
	private CashPaymentPopUpController controller;

    public CashPaymentPopUpModel(CashPaymentPopUpController controller) {
        this.controller = controller;
    }

    public List<String> getPaymentTypes() {
        // Fetch payment types from the mode_of_payment table in the database
        DBManager dbManager = controller.getDBManager();
        return dbManager.query.getPaymentTypes();
    }
}

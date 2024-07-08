package models.transactions;

import controllers.transactions.ViewPaymentsController;
import javafx.collections.ObservableList;
import models.schemas.Payment;

public class ViewPaymentsModel {
    private ViewPaymentsController controller;

    public ViewPaymentsModel(ViewPaymentsController controller) {
        this.controller = controller;
    }

    public ObservableList<Payment> retrievePayments(String query) {
        return this.controller.getDBManager().query.retrievePayments(query);
    }
}

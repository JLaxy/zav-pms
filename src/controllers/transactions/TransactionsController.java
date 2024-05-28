package controllers.transactions;

import controllers.ParentController;
import javafx.fxml.FXML;

public class TransactionsController extends ParentController {
    @FXML
    private void viewtransactions() {
        System.out.println("View Transactions");
    }

    @FXML
    private void viewpayments() {
        System.out.println("View Payments");
    }

    @FXML
    private void goBack() {
        System.out.println("going back...");
        this.borderPaneRootSwitcher.goBack_BP();
    }
}

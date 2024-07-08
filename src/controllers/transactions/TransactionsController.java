package controllers.transactions;

import controllers.ParentController;
import enums.ScreenPaths;
import javafx.fxml.FXML;

public class TransactionsController extends ParentController {
    @FXML
    private void viewtransactions() {
        this.initializeNextScreen_BP(ScreenPaths.Paths.VIEW_TRANSACTIONS.getPath(), loggedInUserInfo, "TRANSACTIONS");
    }

    @FXML
    private void viewpayments() {
        ViewPaymentsController controller = (ViewPaymentsController) this
                .initializeNextScreen_BP(ScreenPaths.Paths.VIEW_PAYMENTS.getPath(), loggedInUserInfo, "PAYMENTS");
        controller.retrievePayments();
    }

    @FXML
    private void goBack() {
        System.out.println("going back...");
        this.borderPaneRootSwitcher.goBack_BP();
    }
}

package controllers.inventory;

import controllers.ParentController;
import javafx.fxml.FXML;

public class RegisterNewStockController extends ParentController {
    @FXML
    private void registernewstocktype() {
        initializeNextScreen_BP("../../views/fxmls/inventory/RegisterNewStockTypeView.fxml", this.loggedInUserInfo, "STOCK INVENTORY");
        System.out.println("Register New Stock Type");
    }

    @FXML
    private void register() {
        System.out.println("Register");
    }

    @FXML
    private void goBack() {
        System.out.println("going back...");
        this.borderPaneRootSwitcher.goBack_BP();
    }

}

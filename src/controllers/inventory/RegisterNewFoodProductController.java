package controllers.inventory;

import controllers.ParentController;
import javafx.fxml.FXML;

public class RegisterNewFoodProductController extends ParentController {
    @FXML
    private void registernewstock() {
        initializeNextScreen_BP("../../views/fxmls/inventory/RegisterNewStockView.fxml", this.loggedInUserInfo, "STOCK INVENTORY");
        System.out.println("Register New Stock");
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

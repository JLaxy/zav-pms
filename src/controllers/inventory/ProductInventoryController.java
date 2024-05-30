package controllers.inventory;

import controllers.ParentController;
import javafx.fxml.FXML;

public class ProductInventoryController extends ParentController {
    @FXML
    private void registernewfood() {
        initializeNextScreen_BP("../../views/fxmls/inventory/RegisterNewFoodProductView.fxml", this.loggedInUserInfo, "PRODUCT INVENTORY");
        System.out.println("Register New Food Product");
    }

    @FXML
    private void registernewbeverage() {
        initializeNextScreen_BP("../../views/fxmls/inventory/RegisterNewBeverageProductView.fxml", this.loggedInUserInfo, "PRODUCT INVENTORY");
        System.out.println("Register New Beverage Product");
    }

    @FXML
    private void goBack() {
        System.out.println("going back...");
        this.borderPaneRootSwitcher.goBack_BP();
    }

    @FXML
    private void viewfoodproduct() {
        System.out.println("View Food Product");
    }

    @FXML
    private void viewbeverageproduct() {
        System.out.println("View Beverage Product");
    }

}

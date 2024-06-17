package controllers.inventory;

import controllers.ParentController;
import enums.ScreenPaths;
import javafx.fxml.FXML;

public class ProductInventoryController extends ParentController {
    @FXML
    private void registerNewFoodVariant() {
        initializeNextScreen_BP(ScreenPaths.Paths.REGISTER_NEW_FOOD_PRODUCT.getPath(), this.loggedInUserInfo,
                "PRODUCT INVENTORY");
        System.out.println("Register New Food Variant");
    }

    @FXML
    private void registerNewBeverageVariant() {
        RegisterNewBeverageVariantController controller = (RegisterNewBeverageVariantController) initializeNextScreen_BP(
                ScreenPaths.Paths.REGISTER_NEW_BEVERAGE_VARIANT.getPath(),
                this.loggedInUserInfo, "PRODUCT INVENTORY");
        controller.initalizeComboBox();
        System.out.println("Register New Beverage Variant");
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

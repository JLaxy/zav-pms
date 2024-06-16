package controllers.inventory;

import controllers.ParentController;
import javafx.fxml.FXML;

public class StockInventoryController extends ParentController {
    @FXML
    private void registernewstock() {
        RegisterNewStockController controller = (RegisterNewStockController) this.initializeNextScreen_BP(
                "../../views/fxmls/inventory/RegisterNewStockView.fxml", this.loggedInUserInfo, "STOCK INVENTORY");
        controller.initializeComboBoxes();
        System.out.println("Register New Stock");
    }

    @FXML
    private void viewstock() {
        System.out.println("View Stock");
        ViewStockInventoryController controller = (ViewStockInventoryController) this.initializeNextScreen_BP(
                "../../views/fxmls/inventory/ViewStockInventoryView.fxml", loggedInUserInfo,
                "VIEW STOCK");
        controller.retrieveStocks(null);
    }

    @FXML
    private void goBack() {
        System.out.println("going back...");
        this.borderPaneRootSwitcher.goBack_BP();
    }

}

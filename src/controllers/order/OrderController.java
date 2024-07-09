package controllers.order;

import controllers.ParentController;
import enums.ScreenPaths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class OrderController extends ParentController {

    @FXML
    private void createOrder() {
        CreateOrderController controller = (CreateOrderController) initializeNextScreen_BP(ScreenPaths.Paths.CREATE_ORDER.getPath(), this.loggedInUserInfo,
        "CREATE ORDER");
        if (controller != null) {
            controller.loadAllProducts(null);
            System.out.println("creating order...");
        }
    }

    @FXML
    private void viewOrders() {
    	ViewOrderController controller=(ViewOrderController)initializeNextScreen_BP(ScreenPaths.Paths.VIEW_ORDER.getPath(), this.loggedInUserInfo,
        "VIEW ORDER");
        System.out.println("viewOrders");
    }

    @FXML
    private void showBackorders() {
        ViewBackOrderController controller = (ViewBackOrderController)initializeNextScreen_BP(ScreenPaths.Paths.VIEW_BACKORDER.getPath(), this.loggedInUserInfo,
        "VIEW BACKORDER");
        System.out.println("showBackorders");
    }
    
    @FXML
    private void showMissingInventory() {
        ViewBackOrderController controller = (ViewBackOrderController)initializeNextScreen_BP(ScreenPaths.Paths.VIEW_BACKORDER.getPath(), this.loggedInUserInfo,
        "VIEW BACKORDER");
        System.out.println("showBackorders");
    }

    @FXML
    private void goBack() {
        System.out.println("going back...");
        this.borderPaneRootSwitcher.goBack_BP();
    }
}

package controllers.inventory;

import controllers.ParentController;
import javafx.fxml.FXML;

public class RegisterNewBeverageProductController extends ParentController {
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

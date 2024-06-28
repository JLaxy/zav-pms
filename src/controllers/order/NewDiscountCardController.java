package controllers.order;

import controllers.ParentController;
import enums.ScreenPaths;
import javafx.fxml.FXML;

public class NewDiscountCardController extends ParentController {

    private SelectDiscountCardController selectdiscountcard;

    @FXML
    private void save() {
        System.out.println("save");
    }

    @FXML
    private void cancel() {
        
        this.borderPaneRootSwitcher.exitPopUpDialog();
    }

    @FXML
    private void goBack() {
        System.out.println("going back...");
        this.borderPaneRootSwitcher.goBack_BP();
    }
}

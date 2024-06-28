package controllers.order;

import controllers.ParentController;
import enums.ScreenPaths;
import javafx.fxml.FXML;

public class DiscountOrdersController extends ParentController {
    @FXML
    private void binddiscountcard() {
        initializeNextScreen_BP(ScreenPaths.Paths.SELECT_DISCOUNT_CARD.getPath(), this.loggedInUserInfo,
        "BIND DISCOUNT CARD");
        System.out.println("bind discount card...");
    }

    @FXML
    private void applydiscount() {
        System.out.println("apply discount");
    }

    @FXML
    private void search() {
        System.out.println("searching");
    }

    @FXML
    private void goBack() {
        System.out.println("going back...");
        this.borderPaneRootSwitcher.goBack_BP();
    }
}

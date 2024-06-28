package controllers.order;

import controllers.ParentController;
import controllers.manageaccounts.UserDetailsController;
import enums.ScreenPaths;
import javafx.fxml.FXML;

public class SelectDiscountCardController extends ParentController {

    @FXML
    private void newdiscountcard() {
        SelectDiscountCardController controller = (SelectDiscountCardController) this
                .initializePopUpDialog(ScreenPaths.Paths.NEW_DISCOUNT_CARD.getPath(), this.loggedInUserInfo);
        System.out.println("new discount card...");
    }

    @FXML
    private void selectdiscountcard() {
        System.out.println("select discount card");
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

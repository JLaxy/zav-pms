package controllers.help;

import controllers.ParentController;
import enums.ScreenPaths;
import enums.UserManualTypes.ManualType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class HowToController extends ParentController {

    @FXML
    private void adminManual(ActionEvent e) {
        UserManualViewerController controller = (UserManualViewerController) this
                .initializeNextScreen_BP(ScreenPaths.Paths.USER_MANUAL.getPath(), loggedInUserInfo, "USER MANUAL");
        controller.initialize(ManualType.ADMIN_MANUAL);
    }

    @FXML
    private void kitchenManual(ActionEvent e) {
        UserManualViewerController controller = (UserManualViewerController) this
                .initializeNextScreen_BP(ScreenPaths.Paths.USER_MANUAL.getPath(), loggedInUserInfo, "USER MANUAL");
        controller.initialize(ManualType.KITCHEN_STAFF_MANUAL);
    }

    @FXML
    private void cashierManual(ActionEvent e) {
        UserManualViewerController controller = (UserManualViewerController) this
                .initializeNextScreen_BP(ScreenPaths.Paths.USER_MANUAL.getPath(), loggedInUserInfo, "USER MANUAL");
        controller.initialize(ManualType.CASHIER_MANUAL);
    }

    @FXML
    private void goBack() {
        this.borderPaneRootSwitcher.goBack_BP();
    }
}

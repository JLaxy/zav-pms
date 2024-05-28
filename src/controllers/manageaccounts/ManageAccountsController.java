package controllers.manageaccounts;

import controllers.ParentController;
import javafx.fxml.FXML;

public class ManageAccountsController extends ParentController {
    @FXML
    private void handleClearAction() {
        System.out.println("Clearing the Text Field...");
    }

    @FXML
    private void viewuserlog() {
        System.out.println("View User Log");
    }

    @FXML
    private void registernewuser() {
        System.out.println("registerNewUser");
    }

    @FXML
    private void edituser() {
        System.out.println("editUser");
    }

    @FXML
    private void goBack() {
        System.out.println("going back...");
        this.borderPaneRootSwitcher.goBack_BP();
    }
}

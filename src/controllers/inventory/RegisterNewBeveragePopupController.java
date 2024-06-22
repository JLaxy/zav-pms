package controllers.inventory;

import controllers.ParentController;
import enums.ScreenPaths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import models.helpers.PopupDialog;
import models.helpers.StringHelper;
import models.inventory.RegisterNewBeveragePopupModel;

public class RegisterNewBeveragePopupController extends ParentController {
    @FXML
    private TextField beverageNameField;

    private RegisterNewBeveragePopupModel model;

    @FXML
    public void initialize() {
        this.model = new RegisterNewBeveragePopupModel(this);
    }

    @FXML
    private void cancel(ActionEvent e) {
        this.borderPaneRootSwitcher.exitPopUpDialog();
    }

    @FXML
    private void save(ActionEvent e) {
        if (beverageNameField.getText().isBlank()) {
            PopupDialog.showCustomErrorDialog("Invalid product name!");
            return;
        }

        if (this.model.doesProductNameAlreadyExist(beverageNameField.getText())) {
            PopupDialog.showCustomErrorDialog("Product name already exists!");
            return;
        }

        // Exit popup dialog
        this.borderPaneRootSwitcher.exitPopUpDialog();

        // Navigate to detail input screen
        RegisterNewBeverageVariantDetailsController controller = (RegisterNewBeverageVariantDetailsController) this
                .initializeNextScreen_BP(ScreenPaths.Paths.REGISTER_NEW_BEVERAGE_VARIANT_DETAILS.getPath(),
                        loggedInUserInfo, "NEW BEVERAGE");
        controller.initialize(StringHelper.toTitleCase(beverageNameField.getText()), true);
    }
}

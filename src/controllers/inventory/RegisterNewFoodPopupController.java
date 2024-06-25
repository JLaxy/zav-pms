package controllers.inventory;

import controllers.ParentController;
import enums.ScreenPaths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import models.helpers.PopupDialog;
import models.helpers.StringHelper;
import models.inventory.RegisterNewFoodPopupModel;

public class RegisterNewFoodPopupController extends ParentController {

    @FXML
    private TextField foodNameField;

    private RegisterNewFoodPopupModel model;

    @FXML
    private void initialize() {
        this.model = new RegisterNewFoodPopupModel(this);
    }

    @FXML
    private void cancel(ActionEvent e) {
        this.borderPaneRootSwitcher.exitPopUpDialog();
    }

    @FXML
    private void save(ActionEvent e) {
        if (this.foodNameField.getText().isBlank()) {
            PopupDialog.showCustomErrorDialog("Invalid product name!");
            return;
        }

        if (this.model.doesFoodProductExist(this.foodNameField.getText())) {
            PopupDialog.showCustomErrorDialog("Product name already exists!");
            return;
        }

        this.borderPaneRootSwitcher.exitPopUpDialog();

        RegisterNewFoodVariantDetailsController controller = (RegisterNewFoodVariantDetailsController) this
                .initializeNextScreen_BP(ScreenPaths.Paths.REGISTER_NEW_FOOD_VARIANT_DETAILS.getPath(),
                        loggedInUserInfo, "NEW FOOD VARIANT");
        controller.initialize(StringHelper.toTitleCase(this.foodNameField.getText()), true);
    }
}

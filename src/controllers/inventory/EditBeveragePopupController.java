package controllers.inventory;

import javax.swing.JOptionPane;

import controllers.ParentController;
import enums.ScreenPaths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import models.helpers.PopupDialog;
import models.inventory.EditBeveragePopupModel;
import models.schemas.DrinkVariant;

public class EditBeveragePopupController extends ParentController {

    private DrinkVariant selectedBeverage;
    private EditBeveragePopupModel model;
    private ViewBeverageProductController viewBeverageProductController;

    @FXML
    public void initialize(DrinkVariant selectedBeverage, ViewBeverageProductController viewBeverageProductController) {
        this.model = new EditBeveragePopupModel(this);
        this.viewBeverageProductController = viewBeverageProductController;
        this.selectedBeverage = selectedBeverage;
    }

    @FXML
    private void cancel(ActionEvent e) {
        this.borderPaneRootSwitcher.exitPopUpDialog();
    }

    @FXML
    private void increaseBeverage(ActionEvent e) {
        IncreaseBeverageController controller = (IncreaseBeverageController) this
                .initializePopUpDialog(ScreenPaths.Paths.INCREASE_BEVERAGE_PRODUCT.getPath(), loggedInUserInfo);
        controller.initialize(this.selectedBeverage, this.viewBeverageProductController);
    }

    @FXML
    private void decreaseBeverage(ActionEvent e) {
        System.out.println("decreasing...");
    }

    @FXML
    private void voidBeverage(ActionEvent e) {
        // Show confirmation dialog
        if (PopupDialog.confirmOperationDialog("Do you really want to void this beverage?") != JOptionPane.YES_OPTION)
            return;

        DrinkVariant voidedBeverage = this.selectedBeverage.getCopy();
        voidedBeverage.toggleVoidStatus();

        if (this.model.editBeverage(this.selectedBeverage, voidedBeverage, loggedInUserInfo)) {
            PopupDialog.showInfoDialog("Voided Beverage", "Successfully voided beverage");
            this.borderPaneRootSwitcher.exitPopUpDialog();
            // Refresh tableview
            viewBeverageProductController.retrieveBeverageProducts();
            return;
        }
        PopupDialog.showCustomErrorDialog("Failed to void beverage!");
        return;
    }
}

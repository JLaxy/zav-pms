package controllers.inventory;

import javax.swing.JOptionPane;

import controllers.ParentController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import models.helpers.PopupDialog;
import models.inventory.EditFoodPopupModel;
import models.schemas.FoodVariant;

public class EditFoodPopupController extends ParentController {

    private EditFoodPopupModel model;
    private FoodVariant selectedFood;
    private ViewFoodProductController viewFoodProductController;

    @FXML
    public void initialize(FoodVariant selectedFood, ViewFoodProductController viewFoodProductController) {
        this.model = new EditFoodPopupModel(this);
        this.viewFoodProductController = viewFoodProductController;
        this.selectedFood = selectedFood;
        System.out.println(selectedFood.getFood_name());
    }

    @FXML
    private void increaseFood(ActionEvent e) {
        System.out.println("increasing...");
    }

    @FXML
    private void decreaseFood(ActionEvent e) {
        System.out.println("decreasing...");
    }

    @FXML
    private void voidFood(ActionEvent e) {
        System.out.println("voiding...");

        // Show confirmation dialog
        if (PopupDialog
                .confirmOperationDialog("Do you really want to void this food variant?") != JOptionPane.YES_OPTION)
            return;

        // Get copy then void
        FoodVariant updatedFood = this.selectedFood.getCopy();
        updatedFood.toggleVoidStatus();

        // Edit then save to database
        if (this.model.editFoodVariant(this.selectedFood, updatedFood,
                loggedInUserInfo)) {
            PopupDialog.showInfoDialog("Success", "Successfully voided food product");
            this.borderPaneRootSwitcher.exitPopUpDialog();
            viewFoodProductController.retrieveFoodProducts();
            return;
        }
        PopupDialog.showCustomErrorDialog("Failed to void food product!");
        return;
    }

    @FXML
    private void cancel(ActionEvent e) {
        this.borderPaneRootSwitcher.exitPopUpDialog();
    }
}

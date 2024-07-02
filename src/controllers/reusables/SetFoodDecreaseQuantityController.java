package controllers.reusables;

import javax.swing.JOptionPane;

import controllers.ParentController;
import controllers.inventory.ViewFoodProductController;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import models.helpers.PopupDialog;
import models.reusables.SetFoodDecreaseQuantityModel;

public class SetFoodDecreaseQuantityController extends ParentController {

    @FXML
    private Spinner<Integer> quantitySpinner;
    @FXML
    private ComboBox<String> reductionTypeCBox;

    private int limitQuantity;
    private SetFoodDecreaseQuantityModel model;
    private ViewFoodProductController viewFoodProductController;

    @FXML
    public void initialize(ViewFoodProductController viewFoodProductController, int limitQuantity) {
        this.model = new SetFoodDecreaseQuantityModel(this);
        this.viewFoodProductController = viewFoodProductController;
        this.limitQuantity = limitQuantity;
        this.configureComboBox();
        this.configureSpinner();
    }

    private void configureComboBox() {
        ObservableList<String> values = this.model.retrieveReductionTypeValues();
        values.remove("used");
        this.reductionTypeCBox.setItems(values);
        this.reductionTypeCBox.getSelectionModel().selectFirst();
    }

    private void configureSpinner() {
        // Configuring quantity spinner
        this.quantitySpinner
                .setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000000000, 1));
        quantitySpinner.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^\\d+(\\.\\d*)?$")) {
                quantitySpinner.getEditor().setText(oldValue);
            }
        });
    }

    @FXML
    private void confirm(ActionEvent e) {
        if (PopupDialog
                .confirmOperationDialog("Do you wish to continue? this cannot be undone!") != JOptionPane.YES_OPTION)
            return;

        if (this.quantitySpinner.getValue() > limitQuantity) {
            PopupDialog.showCustomErrorDialog("Quantity cannot be greater than available quantity!");
            return;
        }
        this.viewFoodProductController.confirmDecrease(this.quantitySpinner.getValue());
    }

    @FXML
    private void cancel(ActionEvent e) {
        this.borderPaneRootSwitcher.exitPopUpDialog();
    }
}

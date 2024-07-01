package controllers.reusables;

import javax.swing.JOptionPane;

import controllers.ParentController;
import controllers.inventory.SelectStockDecreaseController;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import models.helpers.PopupDialog;
import models.reusables.SetDecreaseQuantityModel;

public class SetDecreaseQuantityController extends ParentController {

    @FXML
    private Spinner<Double> quantitySpinner;
    @FXML
    private ComboBox<String> reductionTypeCBox;

    private SelectStockDecreaseController selectStockDecreaseController;
    private SetDecreaseQuantityModel model;
    private double limitQuantity;

    @FXML
    public void initialize(SelectStockDecreaseController selectStockDecreaseController, double limitQuantity) {
        this.limitQuantity = limitQuantity;
        this.model = new SetDecreaseQuantityModel(this);
        this.selectStockDecreaseController = selectStockDecreaseController;
        this.configureSpinner();
        this.configureComboBox();
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
                .setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.5, 1000000000.0, 1.0, 0.1));
        quantitySpinner.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^\\d+(\\.\\d*)?$")) {
                quantitySpinner.getEditor().setText(oldValue);
            }
        });
    }

    @FXML
    private void confirm(ActionEvent e) {
        // Show confirmation dialog
        if (PopupDialog
                .confirmOperationDialog("Do you wish to continue? this cannot be undone!") != JOptionPane.YES_OPTION)
            return;

        if (this.quantitySpinner.getValue() > this.limitQuantity) {
            PopupDialog.showCustomErrorDialog("Quantity cannot be greater than current quantity!");
            return;
        }

        this.borderPaneRootSwitcher.exitPopUpDialog();
        this.selectStockDecreaseController.confirmDecrease(this.quantitySpinner.getValue(),
                this.reductionTypeCBox.getValue());
    }

    @FXML
    private void cancel(ActionEvent e) {
        this.borderPaneRootSwitcher.exitPopUpDialog();
    }
}

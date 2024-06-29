package controllers.inventory;

import java.time.LocalDate;

import javax.swing.JOptionPane;

import controllers.ParentController;
import enums.StockProductType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import models.helpers.PopupDialog;
import models.inventory.IncreaseBeverageModel;
import models.schemas.DrinkVariant;

public class IncreaseBeverageController extends ParentController {

    @FXML
    private TextField costField;
    @FXML
    private DatePicker datePurchasedPicker, dateExpiryPicker;
    @FXML
    private Spinner<Integer> quantitySpinner;

    private IncreaseBeverageModel model;
    private DrinkVariant selectedBeverage;
    private ViewBeverageProductController viewBeverageProductController;

    @FXML
    public void initialize(DrinkVariant selectedBeverage, ViewBeverageProductController viewBeverageProductController) {
        this.model = new IncreaseBeverageModel(this);
        this.viewBeverageProductController = viewBeverageProductController;
        this.selectedBeverage = selectedBeverage;
        this.quantitySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000000000, 0));
        configureCostField();
    }

    // Configures Cost field; Prevents letters from textfield
    private void configureCostField() {
        costField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                    String newValue) {
                if (!newValue.matches("^\\d+(\\.\\d*)?$"))
                    costField.setText(newValue.replaceAll("[^0-9.]", ""));
            }
        });
    }

    // Returns true if all inputs are valid
    private boolean inputsAreValid() {
        if (costField.getText().isBlank()) {
            PopupDialog.showCustomErrorDialog("Beverage cost is not valid!");
            return false;
        }

        try {
            Float.valueOf(costField.getText());
        } catch (Exception e) {
            PopupDialog.showCustomErrorDialog("Beverage cost is not valid!");
            return false;
        }

        if (dateExpiryPicker.getValue() == null) {
            PopupDialog.showCustomErrorDialog("Expiry date is not valid!");
            return false;
        }

        // Expiry date must be before current date
        if (dateExpiryPicker.getValue().isBefore(LocalDate.now())) {
            PopupDialog.showCustomErrorDialog("Expiry date is not valid!");
            return false;
        }

        if (datePurchasedPicker.getValue() == null) {
            PopupDialog.showCustomErrorDialog("Date purchased is not valid!");
            return false;
        }

        if (datePurchasedPicker.getValue().isAfter(LocalDate.now())) {
            PopupDialog.showCustomErrorDialog("Date purchased is not valid!");
            return false;
        }
        return true;
    }

    @FXML
    private void update(ActionEvent e) {
        // Show confirmation dialog
        if (PopupDialog.confirmOperationDialog(
                "Do you want to save this transaction? this cannot be undone!") != JOptionPane.YES_OPTION)
            return;

        if (!inputsAreValid())
            return;

        Double cost = Double.valueOf(costField.getText());
        LocalDate purchaseDate = datePurchasedPicker.getValue();
        LocalDate expiryDate = dateExpiryPicker.getValue();
        int quantity = quantitySpinner.getValue();

        if (!this.model.logBeveragePurchase(selectedBeverage.getId(), quantity, cost,
                purchaseDate, expiryDate,
                StockProductType.Type.BEVERAGE.getValue(),
                loggedInUserInfo, selectedBeverage.getProduct_name())) {
            PopupDialog.showCustomErrorDialog("Failed to log purchase!");
            return;
        }

        // Increment Beverage
        if (!this.model.incrementBeverage(selectedBeverage, quantity, loggedInUserInfo)) {
            PopupDialog.showCustomErrorDialog("Failed to increment quantity! Purchase was recorded.");
            return;
        }

        PopupDialog.showInfoDialog("Incremented Successfully", "Successfully incremented beverage!");
        this.borderPaneRootSwitcher.exitPopUpDialog();

        this.viewBeverageProductController.retrieveBeverageProducts();
    }

    @FXML
    private void cancel(ActionEvent e) {
        this.borderPaneRootSwitcher.exitPopUpDialog();
    }
}

package controllers.inventory;

import javax.swing.JOptionPane;

import controllers.ParentController;
import enums.PreferredUnits;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import models.helpers.PopupDialog;
import models.helpers.NumberHelper;
import models.inventory.RegisterNewBeverageVariantDetailsModel;
import models.schemas.DrinkVariant;

public class RegisterNewBeverageVariantDetailsController extends ParentController {

    @FXML
    private Rectangle sliderRect;
    @FXML
    private Label literLabel, mililiterLabel;
    @FXML
    private TextField regularPriceField, discountedPriceField, sizeField, criticalLevelField;

    private String productName, unitMeasurement = "mL";
    private boolean isNewBeverageProduct;
    private RegisterNewBeverageVariantDetailsModel model;

    @FXML
    public void initialize(String productName, boolean isNewBeverageProduct) {
        this.model = new RegisterNewBeverageVariantDetailsModel(this);
        this.productName = productName;
        this.isNewBeverageProduct = isNewBeverageProduct;

        // Configure fields to only accept numbers
        convertToNumberField(this.regularPriceField);
        convertToNumberField(this.discountedPriceField);
        convertToNumberField(this.sizeField);
        convertToNumberField(this.criticalLevelField);

        System.out.println("product name: " + this.productName);
    }

    @FXML
    private void goBack(ActionEvent e) {
        // Show confirmation dialog
        if (PopupDialog
                .confirmOperationDialog("Are you sure you want to cancel this operation?") != JOptionPane.YES_OPTION)
            return;

        this.borderPaneRootSwitcher.goBack_BP();
    }

    @FXML
    private void register(ActionEvent e) {
        if (!areFieldsValid())
            return;

        Double size = Double.valueOf(sizeField.getText());

        // Converting if needed
        if (this.unitMeasurement.compareTo("L") == 0)
            size = NumberHelper.literToMililiter(size);

        int product_name_id = this.model.getProductNameId(productName);

        // If registered new beverage
        if (isNewBeverageProduct) {
            if (this.model.registerNewBeverage(loggedInUserInfo, productName)) {
                product_name_id = this.model.getProductNameId(productName);
                if (registerNewBeverageVariant(product_name_id, size))
                    return;

            }
        } else {
            if (registerNewBeverageVariant(product_name_id, size))
                return;
        }
        PopupDialog.showCustomErrorDialog("Failed to register beverage variant!");
        this.borderPaneRootSwitcher.goBack_BP(2);
    }

    // Register new beverage variant
    private boolean registerNewBeverageVariant(int product_name_id, double size) {
        if (this.model.registerNewBeverageVariant(loggedInUserInfo,
                new DrinkVariant(-1, productName, product_name_id, size,
                        Float.valueOf(this.regularPriceField.getText()), 0,
                        Integer.valueOf(this.criticalLevelField.getText()), false,
                        Float.valueOf(this.discountedPriceField.getText()),
                        this.unitMeasurement.compareTo("L") == 0 ? PreferredUnits.Units.LITERS.getValue()
                                : PreferredUnits.Units.MILILITERS.getValue()),
                productName)) {
            PopupDialog.showInfoDialog("Success", "Successfully registered new beverage variant");
            this.borderPaneRootSwitcher.goBack_BP(2);
            return true;
        }
        return false;
    }

    // Configures Cost field; Prevents letters from textfield
    private void convertToNumberField(TextField field) {
        field.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                    String newValue) {
                if (!newValue.matches("^\\d+(\\.\\d*)?$"))
                    field.setText(newValue.replaceAll("[^0-9.]", ""));
            }
        });
    }

    @FXML
    private void toggleMeasurement() {
        if (this.unitMeasurement.compareTo("mL") == 0)
            this.unitMeasurement = "L";
        else if (this.unitMeasurement.compareTo("L") == 0)
            this.unitMeasurement = "mL";

        System.out.println(this.unitMeasurement);
        updateButtonState();
    }

    // Returns true if all fields are valid
    private boolean areFieldsValid() {
        // Checking regular
        if (!isFloat(this.regularPriceField.getText(), "Regular Price"))
            return false;
        // Checking discounted price
        else if (!isFloat(this.discountedPriceField.getText(), "Discounted Price"))
            return false;
        // Checking size
        else if (!isFloat(this.sizeField.getText(), "Size"))
            return false;
        else if (!isInt(this.criticalLevelField.getText(), "Critical Level"))
            return false;

        return true;
    }

    // Returns false if supplied is not float
    private boolean isFloat(String floatText, String fieldName) {
        try {
            Float.valueOf(floatText);
            return true;
        } catch (Exception e) {
            PopupDialog.showCustomErrorDialog(fieldName + " field is not valid!");
        }
        return false;
    }

    // Returns false if supplied is not integer
    private boolean isInt(String intText, String fieldName) {
        try {
            Integer.valueOf(intText);
            return true;
        } catch (Exception e) {
            PopupDialog.showCustomErrorDialog(fieldName + " field is not valid!");
        }
        return false;
    }

    // Updates state of custom button element according to selected unitmeasurement
    private void updateButtonState() {
        if (this.unitMeasurement.compareTo("mL") == 0) {
            this.mililiterLabel.setTextFill(Color.BLACK);
            this.literLabel.setTextFill(Color.WHITE);
            this.sliderRect.setLayoutX(1103);
            this.sliderRect.setLayoutY(166);
            return;
        }
        this.mililiterLabel.setTextFill(Color.WHITE);
        this.literLabel.setTextFill(Color.BLACK);
        this.sliderRect.setLayoutX(1174);
        this.sliderRect.setLayoutY(166);
        return;
    }

}
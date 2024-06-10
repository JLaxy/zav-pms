package controllers.inventory;

import controllers.ParentController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class RegisterNewFoodProductController extends ParentController {

    @FXML
    private TextField foodProductNameField;

    @FXML
    private TextField stockRequiredField;

    @FXML
    private TextField regularPriceField;

    @FXML
    private TextField discountedPriceField;

    @FXML
    private ComboBox<String> productTypeComboBox;

    @FXML
    private ComboBox<String> servingSizeComboBox;

    @FXML
    private TextField criticalLevelField;

    @FXML
    private void register() {
        String foodProductName = foodProductNameField.getText().trim();
        String stockRequired = stockRequiredField.getText().trim();
        String regularPrice = regularPriceField.getText().trim();
        String discountedPrice = discountedPriceField.getText().trim();
        String productType = productTypeComboBox.getValue();
        String servingSize = servingSizeComboBox.getValue();
        String criticalLevel = criticalLevelField.getText().trim();

        // Basic validation
        if (foodProductName.isEmpty() || stockRequired.isEmpty() || regularPrice.isEmpty() ||
            discountedPrice.isEmpty() || productType == null || servingSize == null || criticalLevel.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please fill all the fields");
            return;
        }

        try {
            int stockRequiredInt = Integer.parseInt(stockRequired);
            double regularPriceDouble = Double.parseDouble(regularPrice);
            double discountedPriceDouble = Double.parseDouble(discountedPrice);
            int criticalLevelInt = Integer.parseInt(criticalLevel);

            // Here you can add your logic to register the new food product, e.g., save to a database
            System.out.println("Registering new food product:");
            System.out.println("Name: " + foodProductName);
            System.out.println("Stock Required: " + stockRequiredInt);
            System.out.println("Regular Price: " + regularPriceDouble);
            System.out.println("Discounted Price: " + discountedPriceDouble);
            System.out.println("Product Type: " + productType);
            System.out.println("Serving Size: " + servingSize);
            System.out.println("Critical Level: " + criticalLevelInt);

            // Reset fields after successful registration
            resetFields();

            showAlert(Alert.AlertType.INFORMATION, "Registration Successful", "New food product registered successfully");

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter valid numbers for stock, price, and critical level");
        }
    }

    @FXML
    private void registernewstock() {
        // Implement the logic for registering new stock
        System.out.println("Registering new stock...");
    }

    @FXML
    private void goBack() {
        System.out.println("going back...");
        this.borderPaneRootSwitcher.goBack_BP();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void resetFields() {
        foodProductNameField.clear();
        stockRequiredField.clear();
        regularPriceField.clear();
        discountedPriceField.clear();
        productTypeComboBox.setValue(null);
        servingSizeComboBox.setValue(null);
        criticalLevelField.clear();
    }
}

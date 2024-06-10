package controllers.inventory;

import controllers.ParentController;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import models.helpers.PopupDialog;
import models.inventory.RegisterNewStockTypeModel;
import models.schemas.StockType;

public class RegisterNewStockTypeController extends ParentController {

    @FXML
    private TextField stockTypeField, defaultExpiryField;
   
    private int defaultExpiry;

    private RegisterNewStockTypeModel model;

    @FXML
    public void initialize() {
        this.model = new RegisterNewStockTypeModel(this);
    }

    @FXML
    private void register() {       
        // If there is an empty textfield
        if (areFieldsEmpty()) {
            PopupDialog.showCustomErrorDialog("All fields must be filled!");
            return;
        }

        // Check if default expiry is an integer
        if (!isDefaultExpiryNum()) {
            PopupDialog.showCustomErrorDialog("Default expiry must be an integer.");
            return;
        }

        // Check if default expiry is greater than 0
        if (!isDefaultExpiryNotZero()) {
            PopupDialog.showCustomErrorDialog("Default expiry must be greater than 0");
            return;
        }

        StockType newStockType = new StockType(0, stockTypeField.getText(), defaultExpiry);

        this.borderPaneRootSwitcher.goBack_BP();

        // If failed to save new stock type
        if (!this.model.saveNewStockType(newStockType, this.loggedInUserInfo)) {
            PopupDialog.showCustomErrorDialog("Failed to register stock type.");
            return;
        }

        // Else
        PopupDialog.showInfoDialog("Created new stock type", "Stock type registered successfully!");
    }
    
    private boolean isDefaultExpiryNum() {
        String defaultExpiryText = defaultExpiryField.getText().trim();

        try {
            defaultExpiry = Integer.parseInt(defaultExpiryText);
        } catch (NumberFormatException e) {
            return false;
        } 
        return true;
    }

    private boolean isDefaultExpiryNotZero() {
        if (defaultExpiry <= 0) { 
          return false;
        }
        return true;
    }

    // Returns true if fields are not empty
    private boolean areFieldsEmpty() {
        return stockTypeField.getText().isBlank() || defaultExpiryField.getText().isBlank();
    }

    @FXML
    private void goBack() {
        System.out.println("going back...");
        this.borderPaneRootSwitcher.goBack_BP();
    }
}

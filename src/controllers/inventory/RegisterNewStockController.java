package controllers.inventory;

import controllers.ParentController;
import enums.ScreenPaths;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import models.helpers.PopupDialog;
import models.inventory.RegisterNewStockModel;
import models.schemas.Stock;

public class RegisterNewStockController extends ParentController {

    @FXML
    private TextField stockNameField, criticalLevelField;

    @FXML
    private ComboBox<String> unitmeasureCBox, stockTypeCBox;

    private int criticalLevel;

    private RegisterNewStockModel model;

    @FXML
    public void initialize() {
        this.model = new RegisterNewStockModel(this);

    }

    @FXML
    private void register() {
        // If there is an empty textfield
        if (areFieldsEmpty()) {
            PopupDialog.showCustomErrorDialog("All fields must be filled!");
            return;
        }

        // Check if critical level is an integer
        if (!isCriticalLevelNum()) {
            PopupDialog.showCustomErrorDialog("Critical level must be an integer.");
            return;
        }

        // Check if critical level is greater than 0
        if (!isCriticalLevelNotZero()) {
            PopupDialog.showCustomErrorDialog("Critical level must be greater than 0");
            return;
        }

        String unitMeasureString = unitmeasureCBox.getSelectionModel().getSelectedItem();
        String stockTypeString = stockTypeCBox.getSelectionModel().getSelectedItem();

        int unitMeasureID = getDBManager().query.getUnitMeasureID(unitMeasureString);

        Stock newStock = new Stock(0, stockNameField.getText(), 0,
                unitMeasureID,
                (stockTypeString.compareTo("vegetable") == 0 ? 1 : (stockTypeString.compareTo("meat") == 0 ? 2 : 3)),
                criticalLevel, false);

        this.borderPaneRootSwitcher.goBack_BP();

        // If failed to save new stock
        if (!this.model.saveNewStock(newStock, this.loggedInUserInfo)) {
            PopupDialog.showCustomErrorDialog("Failed to register stock.");
            return;
        }

        // Else
        PopupDialog.showInfoDialog("Created new stock", "Stock registered successfully!");
        this.borderPaneRootSwitcher.goBack_BP();
    }

    private boolean isCriticalLevelNum() {
        String criticalLevelText = criticalLevelField.getText().trim();

        try {
            criticalLevel = Integer.parseInt(criticalLevelText);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private boolean isCriticalLevelNotZero() {
        if (criticalLevel <= 0) {
            return false;
        }
        return true;
    }

    // Returns true if fields are not empty
    private boolean areFieldsEmpty() {
        return stockNameField.getText().isBlank() || criticalLevelField.getText().isBlank();
    }

    // Fills Combo boxes
    public void initializeComboBoxes() {
        ObservableList<String> unitMeasure = this.model.getUnitMeasure();
        ObservableList<String> stockTypes = this.model.getStockType();

        // Initialize combo boxes
        this.unitmeasureCBox.setItems(unitMeasure);
        this.stockTypeCBox.setItems(stockTypes);
    }

    @FXML
    private void registernewstocktype() {
        initializeNextScreen_BP(ScreenPaths.Paths.REGISTER_NEW_STOCK_TYPE.getPath(), this.loggedInUserInfo,
                "STOCK INVENTORY");
        System.out.println("Register New Stock Type");
    }

    @FXML
    private void goBack() {
        System.out.println("going back...");
        this.borderPaneRootSwitcher.goBack_BP();
    }

}

package controllers.inventory;

import java.time.LocalDate;
import java.util.Observable;

import javax.swing.JOptionPane;

import controllers.ParentController;
import enums.StockProductType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import models.helpers.PopupDialog;
import models.inventory.IncrementStockInventoryModel;
import models.schemas.Stock;
import models.schemas.StockType;

public class IncrementStockInventoryController extends ParentController {
    @FXML
    private Spinner<Double> quantitySpinner;
    @FXML
    private TextField costField;
    @FXML
    private DatePicker datePurchasedPicker, dateExpiryPicker;

    private ViewStockInventoryController viewStockInventoryController;
    private IncrementStockInventoryModel model;
    private Stock selectedStock;

    @FXML
    public void initialize(Stock selectedStock, ViewStockInventoryController viewStockInventoryController) {
        this.model = new IncrementStockInventoryModel(this);
        this.selectedStock = selectedStock;
        this.quantitySpinner
                .setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(1.0, 1000000000.0, 1.0, 0.1));
        System.out.println("called");
        this.viewStockInventoryController = viewStockInventoryController;
        this.configureFields();
        if(this.selectedStock.getStock_type_id_string().contains("meat") || this.selectedStock.getStock_type_id_string().contains("vegetable")) {
        	dateExpiryPicker.setDisable(true);
        }
    }

    @FXML
    public void cancel(ActionEvent e) {
        this.borderPaneRootSwitcher.exitPopUpDialog();
    }

    @FXML
    public void update(ActionEvent e) {
        // Show confirmation dialog
        if (PopupDialog.confirmOperationDialog(
                "Do you want to save this transaction? this cannot be undone!") != JOptionPane.YES_OPTION)
            return;

        // If inputs are not valid
        if (!inputsAreValid())
            return;

        double cost = Double.valueOf(costField.getText());
        LocalDate purchaseDate = datePurchasedPicker.getValue();
        LocalDate expiryDate = dateExpiryPicker.getValue();
        double quantity = quantitySpinner.getValue();
     // Log Purchase
        if(this.selectedStock.getStock_type_id_string().contains("meat") || this.selectedStock.getStock_type_id_string().contains("vegetable")) {
        	
        	if (!this.model.logStockProductPurchase(this.selectedStock, quantity, cost,
                    purchaseDate, purchaseDate.plusDays(7),
                    StockProductType.Type.STOCK.getValue(), this.loggedInUserInfo)) {
                PopupDialog.showCustomErrorDialog("Failed to log purchase!");
                return;
            }
        }else if(!this.selectedStock.getStock_type_id_string().contains("condiment")) {
        	ObservableList<StockType> stockTypes = this.getDBManager().query.getStockTypes();
        	for(StockType stockType : stockTypes) {
        		if(stockType.getType().contains(this.selectedStock.getStock_type_id_string())) {
        			if(expiryDate==null) {
        				if (!this.model.logStockProductPurchase(this.selectedStock, quantity, cost,
                                purchaseDate,purchaseDate.plusDays(stockType.getDefault_expiration()) ,
                                StockProductType.Type.STOCK.getValue(), this.loggedInUserInfo)) {
                            PopupDialog.showCustomErrorDialog("Failed to log purchase!");
                            return;
                        }
        			}else {
        				if (!this.model.logStockProductPurchase(this.selectedStock, quantity, cost,
                                purchaseDate,expiryDate ,
                                StockProductType.Type.STOCK.getValue(), this.loggedInUserInfo)) {
                            PopupDialog.showCustomErrorDialog("Failed to log purchase!");
                            return;
                        }
        			}
        			
        		}
        	}
        	
        }
        else {
        	if (!this.model.logStockProductPurchase(this.selectedStock, quantity, cost,
                    purchaseDate, expiryDate,
                    StockProductType.Type.STOCK.getValue(), this.loggedInUserInfo)) {
                PopupDialog.showCustomErrorDialog("Failed to log purchase!");
                return;
            }
        }
        
        

        // Increment Stock
        if (!this.model.incrementStock(selectedStock, quantity, loggedInUserInfo)) {
            PopupDialog.showCustomErrorDialog("Failed to increment quantity! Purchase was recorded.");
            return;
        }

        PopupDialog.showInfoDialog("Incremented Successfully", "Successfully incremented stock!");

        this.borderPaneRootSwitcher.exitPopUpDialog();

        // Retrieve stocks from database; Updating table
        this.viewStockInventoryController
                .retrieveStocks(this.viewStockInventoryController.searchField.getText().isBlank() ? null
                        : this.viewStockInventoryController.searchField.getText());
    }

    // Configures Cost field; Prevents letters from textfield
    private void configureFields() {
        // Configuring quantity spinner
        quantitySpinner.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^\\d+(\\.\\d*)?$")) {
                quantitySpinner.getEditor().setText(oldValue);
            }
        });

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
        System.out.println("checking..");

        if (costField.getText().isBlank()) {
            PopupDialog.showCustomErrorDialog("Stock cost is not valid!");
            return false;
        }

        try {
            Double.valueOf(costField.getText());
        } catch (Exception e) {
            PopupDialog.showCustomErrorDialog("Stock cost is not valid!");
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

        if (dateExpiryPicker.getValue() == null && !(this.selectedStock.getStock_type_id_string().contains("meat") || this.selectedStock.getStock_type_id_string().contains("vegetable") || !this.selectedStock.getStock_type_id_string().contains("condiment"))) {
            PopupDialog.showCustomErrorDialog("Expiry date is not valid!");
            return false;
        }
        
        // Expiry date must be before current date
        if(dateExpiryPicker.getValue() != null) {
        	if (dateExpiryPicker.getValue().isBefore(LocalDate.now())) {
                PopupDialog.showCustomErrorDialog("Expiry date is not valid!");
                return false;
            }
        }
        
        

        return true;
    }
}

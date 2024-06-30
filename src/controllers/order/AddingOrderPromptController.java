package controllers.order;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.schemas.Stock;

public class AddingOrderPromptController {

    @FXML
    private Label productNameLabel;
    @FXML
    private Spinner<Integer> quantitySpinner;
    @FXML
    private TableView<StockRequirement> productTableView;
    @FXML
    private TableColumn<StockRequirement, String> stockreqCol;
    @FXML
    private TableColumn<StockRequirement, String> stockstatusCol;

    public void initialize() {
        stockreqCol.setCellValueFactory(new PropertyValueFactory<>("stockRequired"));
        stockstatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    public void setProductName(String productName) {
        productNameLabel.setText(productName);
    }

    public void setSelectedSize(String selectedSize) {
        // This method can be used to set any size-specific logic if needed
    }

    public void setStockRequired(ObservableList<Stock> stockRequired) {
        ObservableList<StockRequirement> stockRequirements = FXCollections.observableArrayList();
        for (Stock stock : stockRequired) {
            stockRequirements.add(new StockRequirement(stock.getStock_name(), checkStockStatus(stock)));
        }
        productTableView.setItems(stockRequirements);
    }

    private String checkStockStatus(Stock stock) {
        return stock.getQuantity() >= stock.getCritical_level() ? "Sufficient" : "Insufficient";
    }

    public static class StockRequirement {
        private final String stockRequired;
        private final String status;

        public StockRequirement(String stockRequired, String status) {
            this.stockRequired = stockRequired;
            this.status = status;
        }

        public String getStockRequired() {
            return stockRequired;
        }

        public String getStatus() {
            return status;
        }
    }

    @FXML
    private void cancel() {
        // Logic to handle cancel action
        System.out.println("Cancel button clicked");
    }

    @FXML
    private void update() {
        // Logic to handle add/update action
        System.out.println("Add button clicked");
    }
}

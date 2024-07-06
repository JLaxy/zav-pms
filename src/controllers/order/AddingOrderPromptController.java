package controllers.order;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.helpers.PopupDialog;
import controllers.ParentController;
import enums.StockProductType;
import models.schemas.OrderProduct;
import models.schemas.Stock;
import models.order.AddingOrderPromptModel;
import models.helpers.database.DBManager;

public class AddingOrderPromptController extends ParentController {

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

    private String productName;
    private String selectedSize;
    private CreateOrderController createOrderController;
    private AddingOrderPromptModel model;

    public void initialize() {
        stockreqCol.setCellValueFactory(new PropertyValueFactory<>("stockRequired"));
        stockstatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Initialize Spinner with a default value
        quantitySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE, 1));
    }

    public void setProductName(String productName) {
        this.productName = productName;
        productNameLabel.setText(productName);
    }

    public void setSelectedSize(String selectedSize) {
        this.selectedSize = selectedSize;
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

    public void setCreateOrderController(CreateOrderController createOrderController) {
        this.createOrderController = createOrderController;
        this.model = new AddingOrderPromptModel(this);
    }

    @FXML
    private void cancel() {
        this.borderPaneRootSwitcher.exitPopUpDialog();
    }

    @FXML
    private void add() {
        Integer quantityValue = quantitySpinner.getValue();
        if (quantityValue == null) {
            PopupDialog.showCustomErrorDialog("Please enter a valid quantity.");
            return;
        }

        int quantity = quantityValue.intValue();
        double amount = quantity * model.fetchAmountFromDatabase(productName, selectedSize, quantity);
        double discountedPrice = quantity * model.fetchDiscountedPriceFromDatabase(productName, selectedSize, quantity);
        boolean stockSufficient = model.checkOverallStockSufficiency(productTableView.getItems(), quantity);

        if (amount == 0.0) {
            // Error already displayed in fetchAmountFromDatabase method
            return;
        }

        OrderProduct orderProduct = new OrderProduct(productName, selectedSize, quantity, amount, discountedPrice, stockSufficient);
        createOrderController.addProductToOrder(orderProduct);

        // Close the AddingOrderPrompt pop-up dialog
        this.borderPaneRootSwitcher.exitPopUpDialog();

        // Show success message
        PopupDialog.showInfoDialog("Notification", "Product successfully added!");

        // Ensure the focus returns to the CreateOrderController
        Platform.runLater(() -> {
            createOrderController.focus();
        });
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
}

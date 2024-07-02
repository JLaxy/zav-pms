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
import models.schemas.Stock;
import models.helpers.PopupDialog;
import controllers.ParentController;
import enums.ScreenPaths;
import enums.StockProductType;
import models.schemas.OrderProduct;
import models.schemas.FoodVariant;
import models.schemas.DrinkVariant;
import models.order.SelectSizeModel;
import models.order.CreateOrderModel;

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
    private SelectSizeModel model;

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
        this.model = new SelectSizeModel(new CreateOrderModel(createOrderController));
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
        double amount = fetchAmountFromDatabase(productName, selectedSize, quantity);
        double discountedPrice = fetchDiscountedPriceFromDatabase(productName, selectedSize, quantity);
        boolean stockSufficient = checkOverallStockSufficiency();

        if (amount == 0.0) {
            // Error already displayed in fetchAmountFromDatabase method
            return;
        }

        if (!stockSufficient) {
            PopupDialog.showCustomErrorDialog("Stock required for the product is insufficient. Unable to add the product.");
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

    private double fetchAmountFromDatabase(String productName, String selectedSize, int quantity) {
        int productId = model.getProductId(productName);
        StockProductType.Type productType = model.getProductType(productId);

        if (productType == StockProductType.Type.BEVERAGE) {
            DrinkVariant drinkVariant = model.getDrinkVariantBySize(productId, selectedSize);
            if (drinkVariant != null) {
                return drinkVariant.getPrice() * quantity;
            } else {
                PopupDialog.showCustomErrorDialog("Error: Drink variant not found.");
                return 0.0;
            }
        } else if (productType == StockProductType.Type.FOOD) {
            FoodVariant foodVariant = model.getFoodVariantBySize(productId, selectedSize);
            if (foodVariant != null) {
                return foodVariant.getRegular_price() * quantity;
            } else {
                PopupDialog.showCustomErrorDialog("Error: Food variant not found.");
                return 0.0;
            }
        }
        return 0.0; // Default fallback, should not reach here ideally
    }

    private double fetchDiscountedPriceFromDatabase(String productName, String selectedSize, int quantity) {
        int productId = model.getProductId(productName);
        StockProductType.Type productType = model.getProductType(productId);

        if (productType == StockProductType.Type.BEVERAGE) {
            DrinkVariant drinkVariant = model.getDrinkVariantBySize(productId, selectedSize);
            if (drinkVariant != null) {
                return drinkVariant.getDiscounted_price() * quantity;
            } else {
                PopupDialog.showCustomErrorDialog("Error: Drink variant not found.");
                return 0.0;
            }
        } else if (productType == StockProductType.Type.FOOD) {
            FoodVariant foodVariant = model.getFoodVariantBySize(productId, selectedSize);
            if (foodVariant != null) {
                return foodVariant.getDiscounted_price() * quantity;
            } else {
                PopupDialog.showCustomErrorDialog("Error: Food variant not found.");
                return 0.0;
            }
        }
        return 0.0; // Default fallback, should not reach here ideally
    }

    private boolean checkOverallStockSufficiency() {
        for (StockRequirement requirement : productTableView.getItems()) {
            if ("Insufficient".equals(requirement.getStatus())) {
                return false;
            }
        }
        return true;
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

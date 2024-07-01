package controllers.order;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import java.util.Map;
import java.util.HashMap;
import controllers.ParentController;
import enums.ScreenPaths;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.order.CreateOrderModel;
import models.schemas.FoodVariant;
import models.schemas.DrinkVariant;

public class CreateOrderController extends ParentController {

    @FXML
    private Button goBackButton, searchButton, allCategory, foodCategory, beverageCategory, removeProductButton,
            discountProductButton, createTransactionButton;

    @FXML        
    private Label productTitleLabel;

    @FXML
    private ComboBox<String> sizeComboBox;

    @FXML
    private TextField searchField;

    @FXML
    private TilePane productContainer;

    private ObservableList<FoodVariant> foodItems;
    private ObservableList<DrinkVariant> beverages;
    private CreateOrderModel model;

    public void initialize() {
        this.model = new CreateOrderModel(this);
    }

    @FXML
    public void loadAllProducts(ActionEvent event) {
        loadProducts(null, null);
    }

    @FXML
    private void loadFoodProducts(ActionEvent event) {
        loadProducts("food", null);
    }

    @FXML
    private void loadBeverageProducts(ActionEvent event) {
        loadProducts("beverage", null);
    }
    
    public void loadProducts(String category, String query) {
        productContainer.getChildren().clear();
        
        System.out.println("showing loading screen");
        // Show loading Screen
        borderPaneRootSwitcher.showLoadingScreen_BP();

        Service<Void> productRetriever = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        try {
                            foodItems = FXCollections.observableArrayList();
                            beverages = FXCollections.observableArrayList();

                            if (category == null || category.equals("food")) {
                                foodItems = model.getFoodProducts(query);
                            }
                            if (category == null || category.equals("beverage")) {
                                beverages = model.getDrinkProducts(query);
                            }

                            // Group products by name
                            Map<String, Product> productMap = new HashMap<>();
                            for (FoodVariant food : foodItems) {
                                productMap.computeIfAbsent(food.getFood_name(), k -> new Product(food.getFood_name())).addSize(food.getServing_size_id_string());
                            }
                            for (DrinkVariant drink : beverages) {
                                productMap.computeIfAbsent(drink.getProduct_name(), k -> new Product(drink.getProduct_name())).addSize(drink.getSize_string());
                            }

                            List<Product> allProducts = new ArrayList<>(productMap.values());
                            allProducts.sort(Comparator.comparing(Product::getName));

                            for (Product product : allProducts) {
                                VBox buttonBox = createProductButton(product.getName(), product.isFastMoving(), product.isStockSufficient());
                                Platform.runLater(() -> productContainer.getChildren().add(buttonBox));
                            }
                            System.out.println("finished");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        return null;
                    }
                };
            }
        };

        productRetriever.setOnSucceeded(e -> {
            this.borderPaneRootSwitcher.exitLoadingScreen_BP();
        });

        productRetriever.start();
    }

    private VBox createProductButton(String productName, boolean isFastMoving, boolean isStockSufficient) {
        Label productNameLabel = new Label(productName);
        productNameLabel.setStyle("-fx-alignment: center; -fx-font-size: 15px; -fx-text-fill: black;");
        productNameLabel.setMaxWidth(Double.MAX_VALUE);
        productNameLabel.setWrapText(true);
    
        Label movingStatusLabel = new Label(isFastMoving ? "Fast Moving" : "Slow Moving");
        movingStatusLabel.setStyle("-fx-alignment: center; -fx-font-size: 12px; -fx-text-fill: " + (isFastMoving ? "green;" : "red;"));
        movingStatusLabel.setMaxWidth(Double.MAX_VALUE);
        movingStatusLabel.setWrapText(true);
    
        Label stockStatusLabel = new Label(isStockSufficient ? "In Stock" : "Insufficient Stock");
        stockStatusLabel.setStyle("-fx-alignment: center; -fx-font-size: 12px; -fx-text-fill: " + (isStockSufficient ? "green;" : "red;"));
        stockStatusLabel.setMaxWidth(Double.MAX_VALUE);
        stockStatusLabel.setWrapText(true);
    
        VBox vbox = new VBox(movingStatusLabel, productNameLabel, stockStatusLabel);
        vbox.setStyle("-fx-alignment: center; -fx-background-color: white; -fx-spacing: 10px; -fx-padding: 10px; -fx-background-radius: 10px;");
        vbox.setPrefSize(130, 130);  // Set preferred width and height
        vbox.setMinSize(130, 130);   // Set minimum width and height
        vbox.setMaxSize(130, 130);   // Set maximum width and height
    
        vbox.setOnMouseClicked(event -> {
            SelectSizeController controller = (SelectSizeController) this
                .initializePopUpDialog(ScreenPaths.Paths.SELECT_SIZE.getPath(), this.loggedInUserInfo);
            controller.setProductName(productName); // Ensure productName is set before initializing the dialog
            controller.setOrderController(this); // Properly set the controller
            System.out.println("Selected product: " + productName);
        });
    
        return vbox;
    }
    
    
    @FXML
    private void cancel(ActionEvent e) {
        this.borderPaneRootSwitcher.exitPopUpDialog();
    }

    @FXML
    private void select() {
        System.out.println("Select");
    }

    private boolean isFastMovingProduct(FoodVariant food) {
        // Implement actual logic to determine if the food product is fast-moving
        return true; // Placeholder
    }

    private boolean isStockSufficient(FoodVariant food) {
        // Implement actual logic to determine if the stock is sufficient for the food product
        return true; // Placeholder
    }

    private boolean isFastMovingProduct(DrinkVariant drink) {
        // Implement actual logic to determine if the drink product is fast-moving
        return true; // Placeholder
    }

    private boolean isStockSufficient(DrinkVariant drink) {
        // Implement actual logic to determine if the stock is sufficient for the drink product
        return true; // Placeholder
    }

    @FXML
    private void createtransaction(ActionEvent event) {
        // Implement transaction creation logic here
    }

    @FXML
    public void removeproduct(ActionEvent event) {
        System.out.println("remove product");
    }

    @FXML
    public void discountproducts(ActionEvent event) {
        initializeNextScreen_BP(ScreenPaths.Paths.DISCOUNT_ORDERS.getPath(), this.loggedInUserInfo,
        "DISCOUNT ORDERS");
        System.out.println("discount products");
    }

    @FXML
    public void search(ActionEvent event) {
        loadProducts(null, searchField.getText());
        System.out.println("search");
    }

    @FXML
    private void goBack(ActionEvent event) {
        System.out.println("going back...");
        this.borderPaneRootSwitcher.goBack_BP();
    }
    private static class Product {
        private final String name;
        private final boolean isFastMoving;
        private final boolean isStockSufficient;
        private final List<String> sizes;

        public Product(String name) {
            this.name = name;
            this.isFastMoving = true; // Placeholder logic
            this.isStockSufficient = true; // Placeholder logic
            this.sizes = new ArrayList<>();
        }

        public String getName() {
            return name;
        }

        public boolean isFastMoving() {
            return isFastMoving;
        }

        public boolean isStockSufficient() {
            return isStockSufficient;
        }

        public List<String> getSizes() {
            return sizes;
        }

        public void addSize(String size) {
            sizes.add(size);
        }
    }
}

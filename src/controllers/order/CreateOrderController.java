package controllers.order;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import models.helpers.ObservableListHelper;
import models.helpers.PopupDialog;
import models.order.CreateOrderModel;
import models.schemas.DrinkVariant;
import models.schemas.FoodVariant;
import models.schemas.OrderProduct;
import controllers.ParentController;
import controllers.transactions.CreateTransactionsController;
import enums.ScreenPaths;
import models.schemas.Stock;

import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateOrderController extends ParentController {

    @FXML
    private TableView<OrderProduct> orderTableView;
    @FXML
    private TableColumn<OrderProduct, String> productNameCol;
    @FXML
    private TableColumn<OrderProduct, String> sizeCol;
    @FXML
    private TableColumn<OrderProduct, Integer> quantityCol;
    @FXML
    private TableColumn<OrderProduct, Double> amountCol;
    @FXML
    private TableColumn<OrderProduct, String> discountedCol;

    @FXML
    private Button goBackButton, searchButton, allCategory, foodCategory, beverageCategory, removeProductButton,
            discountProductButton, createTransactionButton;

    @FXML
    private TextField searchField;

    @FXML
    private TilePane productContainer;

    private ObservableList<FoodVariant> foodItems;
    private ObservableList<DrinkVariant> beverages;
    private CreateOrderModel model;
    private ObservableList<OrderProduct> orderProducts;

    public void initialize() {
        this.model = new CreateOrderModel(this);
        this.orderProducts = FXCollections.observableArrayList();
        
        productNameCol.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());
        sizeCol.setCellValueFactory(cellData -> cellData.getValue().sizeProperty());
        quantityCol.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
        amountCol.setCellValueFactory(cellData -> cellData.getValue().amountProperty().asObject());
        discountedCol.setCellValueFactory(cellData -> cellData.getValue().discountedProperty());
    
        discountedCol.setCellFactory(column -> {
            return new TableCell<OrderProduct, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        System.out.println("falase");
                        setText(null);
                        setGraphic(null);
                    } else {
                        System.out.println("true");
                        setText(item);
                        setStyle("-fx-alignment: CENTER;");
                    }
                }
            };
        });
    
        orderTableView.setItems(this.orderProducts);
    
        orderTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                removeProductButton.setVisible(true);
            } else {
                removeProductButton.setVisible(false);
            }
        });
    
        // Hide the removeProductButton initially
        removeProductButton.setVisible(false);
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
                                VBox buttonBox = createProductButton(product.getName(), product.isFastMoving(), model.isStockSufficient(product.getName()));
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
        vbox.setPrefSize(130, 130);
        vbox.setMinSize(130, 130);
        vbox.setMaxSize(130, 130);

        vbox.setOnMouseClicked(event -> {
            SelectSizeController controller = (SelectSizeController) this
                .initializePopUpDialog(ScreenPaths.Paths.SELECT_SIZE.getPath(), this.loggedInUserInfo);
            if (controller != null) {
                controller.setProductName(productName);
                controller.setOrderController(this);
                System.out.println("Selected product: " + productName);
            }
        });

        return vbox;
    }
        
    public void addProductToOrder(OrderProduct orderProduct) {
        boolean productExists = false;
        // for (OrderProduct existingProduct : orderProducts) {
        //     if (existingProduct.getProductName().equals(orderProduct.getProductName()) &&
        //         existingProduct.getSize().equals(orderProduct.getSize())) {
    
        //         // Update the existing product's quantity
        //         int newQuantity = existingProduct.getQuantity() + orderProduct.getQuantity();
        //         existingProduct.setQuantity(newQuantity);
    
        //         // Update the initial values for consistent calculation
        //         existingProduct.initialQuantity += orderProduct.getQuantity();
        //         existingProduct.initialAmount += orderProduct.getInitialAmount();
    
        //         // Recalculate the amount
        //         existingProduct.calculateAmount();
    
        //         productExists = true;
        //         break;
        //     }
        // }
        if (!productExists) {
            this.orderProducts.add(orderProduct);
        }
    
        if (!orderProduct.isStockSufficient()) {
            PopupDialog.showCustomErrorDialog("Stock for product " + orderProduct.getProductName() + " is insufficient. It will be added as a backorder.");
        }
    
        orderTableView.refresh();
        Platform.runLater(() -> {
            this.borderPaneRootSwitcher.exitPopUpDialog();
            orderTableView.requestFocus();
        });
    }            
                
    @FXML
    private void cancel(ActionEvent e) {
        this.borderPaneRootSwitcher.exitPopUpDialog();
    }

    @FXML
    private void select() {
        System.out.println("Select");
    }

    @FXML
    private void createtransaction(ActionEvent event) {
        CreateTransactionsController controller = (CreateTransactionsController)
        initializeNextScreen_BP(ScreenPaths.Paths.CREATE_TRANSACTION.getPath(), this.loggedInUserInfo, "CREATE TRANSACTION");
        controller.initialize(this.orderProducts);
    }

    @FXML
    private void removeproduct(ActionEvent event) {
        OrderProduct selectedProduct = orderTableView.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            int result = PopupDialog.confirmOperationDialog("Are you sure you want to remove the selected product? Product: " + selectedProduct.getProductName());
            if (result == JOptionPane.YES_OPTION) {
                orderProducts.remove(selectedProduct);
                orderTableView.refresh();
                removeProductButton.setVisible(false);
                orderTableView.getSelectionModel().clearSelection();
            }
        }
    }

    @FXML
    public void discountproducts(ActionEvent event) {
        // ObservableList<OrderProduct> nonDiscountedProducts = orderProducts.filtered(op -> op.getRemainingQuantity() > 0);
        // if (nonDiscountedProducts.isEmpty()) {
        //     PopupDialog.showCustomErrorDialog("There are no products left to be discounted.");
        //     return;
        // }
        DiscountOrdersController controller = (DiscountOrdersController) initializeNextScreen_BP(ScreenPaths.Paths.DISCOUNT_ORDERS.getPath(), this.loggedInUserInfo, "DISCOUNT ORDERS");
        controller.setCreateOrderController(this);
        System.out.println(this.orderProducts);
        controller.setOrderProducts(ObservableListHelper.getRegularOnList(this.orderProducts));
    }

    @FXML
    public void search(ActionEvent event) {
        loadProducts(null, searchField.getText());
        System.out.println("search");
    }

    public void focus() {
        // Implement any necessary updates or focus changes required after adding a product
        orderTableView.requestFocus(); // Example: setting focus back to the order table view
    }

    public void updateOrderProducts(ObservableList<OrderProduct> updatedOrderProducts) {
        System.out.println("hhgyugyug: "+ updatedOrderProducts);

        this.orderProducts = ObservableListHelper.getDiscountedOnList(this.orderProducts);
        this.orderProducts.addAll(updatedOrderProducts);

        this.orderTableView.setItems(this.orderProducts);
        this.orderTableView.refresh();
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
            this.isFastMoving = true;
            this.isStockSufficient = true;
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
package controllers.order;

import enums.StockProductType;
import controllers.ParentController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import models.helpers.PopupDialog;
import models.order.CreateOrderModel;
import models.order.SelectSizeModel;
import models.schemas.FoodVariant;
import models.schemas.Stock;

import java.io.IOException;

public class SelectSizeController extends ParentController {

    @FXML
    private ComboBox<String> sizeComboBox;

    private String productName;
    private SelectSizeModel model;
    private CreateOrderController orderController;

    public void setProductName(String productName) {
        this.productName = productName;
        System.out.println("Product Name set to: " + productName); // Debugging statement
    }

    public void setOrderController(CreateOrderController orderController) {
        this.orderController = orderController;
        CreateOrderModel createOrderModel = new CreateOrderModel(orderController);
        this.model = new SelectSizeModel(createOrderModel);  // Initialize model with the controller
        System.out.println("Order Controller set and model initialized."); // Debugging statement
        loadSizes(); // Load sizes after initializing the model and setting the product name
    }

    public void loadSizes() {
        if (this.productName == null) {
            System.out.println("Product name is null, cannot load sizes."); // Debugging statement
            return;
        }
        ObservableList<String> sizes = FXCollections.observableArrayList();
        int productId = model.getProductId(productName);
        System.out.println("Product ID: " + productId); // Debugging statement

        StockProductType.Type productType = model.getProductType(productId);
        if (productType == StockProductType.Type.BEVERAGE) {
            sizes = model.getDrinkSizes(productId);
        } else if (productType == StockProductType.Type.FOOD) {
            sizes = model.getFoodSizes(productId);
        }
        System.out.println("Sizes loaded: " + sizes); // Debugging statement
        sizeComboBox.setItems(sizes);
        System.out.println("ComboBox items set: " + sizeComboBox.getItems()); // Debugging statement
    }

    @FXML
    private void select(ActionEvent e) {
        String selectedSize = sizeComboBox.getValue();
        if (selectedSize == null || selectedSize.isEmpty()) {
            PopupDialog.showCustomErrorDialog("Please select a size before proceeding.");
        } else {
            System.out.println("Selected size: " + selectedSize);

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../../views/fxmls/order/AddingOrderPrompt.fxml")); // Ensure the path is correct
                Parent root = loader.load();

                AddingOrderPromptController controller = loader.getController();
                controller.setProductName(productName);
                controller.setSelectedSize(selectedSize);

                int productId = model.getProductId(productName);
                StockProductType.Type productType = model.getProductType(productId);
                if (productType == StockProductType.Type.FOOD) {
                    FoodVariant foodVariant = model.getFoodVariantBySize(productId, selectedSize);
                    controller.setStockRequired(model.getStockRequirements(foodVariant));
                }

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();

                this.borderPaneRootSwitcher.exitPopUpDialog();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @FXML
    private void cancel(ActionEvent e) {
        this.borderPaneRootSwitcher.exitPopUpDialog();
    }
}

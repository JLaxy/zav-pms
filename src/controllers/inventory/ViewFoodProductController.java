package controllers.inventory;

import controllers.ParentController;
import enums.ScreenPaths;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import models.helpers.PopupDialog;
import models.inventory.ViewFoodProductModel;
import models.schemas.FoodVariant;

public class ViewFoodProductController extends ParentController {

    @FXML
    private Label productNameLabel, servingSizeLabel, priceLabel, discountedPriceLabel, inventoryCountLabel;
    @FXML
    private TableView<FoodVariant> foodTable;
    @FXML
    private TableColumn<FoodVariant, String> servingSizeCol, foodNameCol;
    @FXML
    private TextField searchField;
    @FXML
    private VBox foodDetailsVBox;
    @FXML
    private Button editFoodButton;

    private FoodVariant selectedFood;
    private ViewFoodProductModel model;

    @FXML
    public void initialize() {
        this.model = new ViewFoodProductModel(this);
        this.foodDetailsVBox.setVisible(false);
        this.editFoodButton.setVisible(false);
        this.configureTable();
        this.configureOnClick();
    }

    // Configuring tableview
    private void configureTable() {
        servingSizeCol.setCellValueFactory(new PropertyValueFactory<FoodVariant, String>("serving_size_id_string"));
        foodNameCol.setCellValueFactory(new PropertyValueFactory<FoodVariant, String>("food_name"));

        foodTable.getColumns().forEach(e -> {
            e.setReorderable(false);
        });
    }

    public void retrieveFoodProducts() {
        try {
            // Show loading Screen
            this.borderPaneRootSwitcher.showLoadingScreen_BP();

            // Create thread
            Service<Void> foodProductsRetriever = new Service<Void>() {
                @Override
                protected Task<Void> createTask() {
                    return new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            // Get retrieved value
                            foodTable.setItems(model
                                    .getFoodProducts(searchField.getText().isBlank() ? null : searchField.getText()));
                            return null;
                        }
                    };
                }
            };
            foodProductsRetriever.setOnSucceeded(e -> {
                // Exit Loading Screen
                borderPaneRootSwitcher.exitLoadingScreen_BP();
            });
            foodProductsRetriever.start();
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
        }
    }

    private void configureOnClick() {
        this.foodTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<FoodVariant>() {
            @Override
            public void changed(ObservableValue<? extends FoodVariant> arg0, FoodVariant arg1, FoodVariant arg2) {
                // If has selected
                if (foodTable.getSelectionModel().getSelectedItem() != null) {
                    selectedFood = foodTable.getSelectionModel().getSelectedItem();
                    productNameLabel.setText(selectedFood.getFood_name());
                    servingSizeLabel.setText(selectedFood.getServing_size_id_string());
                    priceLabel.setText(String.valueOf(selectedFood.getRegular_price()));
                    discountedPriceLabel.setText(String.valueOf(selectedFood.getDiscounted_price()));
                    inventoryCountLabel.setText(String.valueOf(selectedFood.getAvailable_count()));

                    editFoodButton.setVisible(true);
                    foodDetailsVBox.setVisible(true);
                    return;
                }

                editFoodButton.setVisible(false);
                foodDetailsVBox.setVisible(false);
            }
        });
    }

    @FXML
    public void editFood(ActionEvent e) {
        EditFoodPopupController controller = (EditFoodPopupController) this
                .initializePopUpDialog(ScreenPaths.Paths.EDIT_FOOD_PRODUCT.getPath(), loggedInUserInfo);
        controller.initialize(selectedFood, this);
    }

    public void confirmDecrease(int quantity) {
        System.out.println("decreasing " + quantity);
    }

    @FXML
    public void goBack(ActionEvent e) {
        this.borderPaneRootSwitcher.goBack_BP();
    }

    @FXML
    public void search() {
        retrieveFoodProducts();
    }

}

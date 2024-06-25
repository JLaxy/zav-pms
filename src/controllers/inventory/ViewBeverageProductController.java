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
import models.helpers.NumberHelper;
import models.inventory.ViewBeverageProductModel;
import models.schemas.DrinkVariant;

public class ViewBeverageProductController extends ParentController {

    @FXML
    private TableView<DrinkVariant> beverageTable;
    @FXML
    private TableColumn<DrinkVariant, String> beverageNameCol, sizeCol;
    @FXML
    private Button editBeverageButton;
    @FXML
    private TextField searchField;
    @FXML
    private VBox beverageDetailsVBox;
    @FXML
    private Label productNameLabel, sizeLabel, priceLabel, discountedPriceLabel, inventoryCountLabel,
            criticalLevelLabel;

    private DrinkVariant selectedItem;
    private ViewBeverageProductModel model;

    @FXML
    public void initialize() {
        this.model = new ViewBeverageProductModel(this);
        beverageDetailsVBox.setVisible(false);
        editBeverageButton.setVisible(false);
        configureTableView();
        configureTableOnClick();
    }

    // Configure on-click action of button
    private void configureTableOnClick() {
        // Update details on selection on table
        this.beverageTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<DrinkVariant>() {
            @Override
            public void changed(ObservableValue<? extends DrinkVariant> arg0, DrinkVariant arg1, DrinkVariant arg2) {
                selectedItem = beverageTable.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    // Updating values
                    productNameLabel.setText(selectedItem.getProduct_name());
                    sizeLabel.setText(selectedItem.getSize_string());
                    priceLabel.setText(NumberHelper.toTwoDecimalPlaces(selectedItem.getPrice()));
                    discountedPriceLabel.setText(NumberHelper.toTwoDecimalPlaces(selectedItem.getDiscounted_price()));
                    inventoryCountLabel.setText(String.valueOf(selectedItem.getAvailable_count()));
                    criticalLevelLabel.setText(String.valueOf(selectedItem.getCritical_level()));

                    // Showing elements
                    beverageDetailsVBox.setVisible(true);
                    editBeverageButton.setVisible(true);
                }
            }
        });
    }

    // Configure Columns of Table View
    private void configureTableView() {
        beverageNameCol.setCellValueFactory(new PropertyValueFactory<DrinkVariant, String>("product_name"));
        sizeCol
                .setCellValueFactory(new PropertyValueFactory<DrinkVariant, String>("size_string"));

        // Disable column reodering
        beverageTable.getColumns().forEach(e -> {
            e.setReorderable(false);
        });
    }

    // Retrieves beverage products on database
    public void retrieveBeverageProducts() {
        beverageDetailsVBox.setVisible(false);
        editBeverageButton.setVisible(false);
        try {
            // Show loading Screen
            this.borderPaneRootSwitcher.showLoadingScreen_BP();

            // Retrieves beverages on database
            Service<Void> beverageProductRetriever = new Service<Void>() {
                @Override
                protected Task<Void> createTask() {
                    return new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            beverageTable.setItems(model.getBeverageProducts(
                                    searchField.getText().isBlank() ? null : searchField.getText()));
                            return null;
                        }
                    };
                }
            };

            beverageProductRetriever.setOnSucceeded(e -> {
                // Exit Loading Screen
                borderPaneRootSwitcher.exitLoadingScreen_BP();
            });

            beverageProductRetriever.start();
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
        }
    }

    @FXML
    private void goBack(ActionEvent e) {
        this.borderPaneRootSwitcher.goBack_BP();
    }

    @FXML
    private void search(ActionEvent e) {
        retrieveBeverageProducts();
    }

    @FXML
    private void editBeverage(ActionEvent e) {
        EditBeveragePopupController controller = (EditBeveragePopupController) this
                .initializePopUpDialog(ScreenPaths.Paths.EDIT_BEVERAGE_PRODUCT.getPath(), loggedInUserInfo);
        controller.initialize(this.selectedItem, this);
    }
}

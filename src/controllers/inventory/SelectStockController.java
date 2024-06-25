package controllers.inventory;

import controllers.ParentController;
import controllers.reusables.SetQuantityController;
import enums.ScreenPaths;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import models.helpers.PopupDialog;
import models.inventory.SelectStockModel;
import models.schemas.Stock;

public class SelectStockController extends ParentController {

    @FXML
    private TableView<Stock> stockTable;
    @FXML
    private TableColumn<Stock, String> stockNameCol, quantityCol;
    @FXML
    private Label stockNameLabel, stockTypeLabel, quantityLabel, criticalLevelLabel, unitMeasureLabel;
    @FXML
    private AnchorPane stockDetailsPane;
    @FXML
    private TextField searchField;

    private Stock selectedStock;
    private SelectStockModel model;
    private RegisterNewFoodVariantDetailsController registerNewFoodVariantDetailsController;

    @FXML
    public void initialize(RegisterNewFoodVariantDetailsController registerNewFoodVariantDetailsController) {
        this.model = new SelectStockModel(this);
        this.registerNewFoodVariantDetailsController = registerNewFoodVariantDetailsController;
        this.stockDetailsPane.setVisible(false);
        configureStockTable();
        configureOnClick();
    }

    // Configuring stock table
    private void configureStockTable() {
        stockNameCol.setCellValueFactory(new PropertyValueFactory<Stock, String>("stock_name"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<Stock, String>("quantity"));

        stockTable.getColumns().forEach(e -> {
            e.setReorderable(false);
        });
    }

    public void retrieveStocks(String stockName) {
        try {
            // Show loading Screen
            this.borderPaneRootSwitcher.showLoadingScreen_BP();

            // Create thread
            Service<Void> stockRetriever = new Service<Void>() {
                @Override
                protected Task<Void> createTask() {
                    return new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            // Get retrieved value
                            stockTable.setItems(model.getStocks(stockName));
                            return null;
                        }
                    };
                }
            };
            stockRetriever.setOnSucceeded(e -> {
                // Exit Loading Screen
                borderPaneRootSwitcher.exitLoadingScreen_BP();
            });
            stockRetriever.start();
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
        }
    }

    private void configureOnClick() {
        this.stockTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Stock>() {
            @Override
            public void changed(ObservableValue<? extends Stock> arg0, Stock arg1, Stock arg2) {
                // If has selected
                if (stockTable.getSelectionModel().getSelectedItem() != null) {
                    selectedStock = stockTable.getSelectionModel().getSelectedItem();

                    stockNameLabel.setText(selectedStock.getStock_name());
                    stockTypeLabel.setText(selectedStock.getStock_type_id_string());
                    quantityLabel.setText(String.valueOf(selectedStock.getQuantity()));
                    criticalLevelLabel.setText(String.valueOf(selectedStock.getCritical_level()));
                    unitMeasureLabel.setText(selectedStock.getUnit_measure_id_string());

                    stockDetailsPane.setVisible(true);
                }
            }
        });
    }

    @FXML
    private void goBack(ActionEvent e) {
        this.borderPaneRootSwitcher.goBack_BP();
    }

    @FXML
    private void selectStock(ActionEvent e) {
        System.out.println("selected stock: " + this.selectedStock.getStock_name());
        SetQuantityController controller = (SetQuantityController) this
                .initializePopUpDialog(ScreenPaths.Paths.SET_QUANTITY.getPath(), loggedInUserInfo);
        controller.initialize(this);
    }

    // Add stock to required stocks
    public void confirmSelection(int quantity) {
        this.selectedStock.setSelectedQuantity(quantity);
        this.registerNewFoodVariantDetailsController.addRequiredStock(this.selectedStock);
    }

    @FXML
    private void search(ActionEvent e) {
        stockDetailsPane.setVisible(false);
        retrieveStocks(searchField.getText().isBlank() ? null : searchField.getText());
    }
}

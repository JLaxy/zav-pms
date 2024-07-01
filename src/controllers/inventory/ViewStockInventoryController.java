package controllers.inventory;

import controllers.ParentController;
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
import models.inventory.ViewStockInventoryModel;
import models.schemas.Stock;

public class ViewStockInventoryController extends ParentController {

    @FXML
    private TableColumn<Stock, String> stockNameCol, stockTypeCol, unitMeasureCol;
    @FXML
    private TableColumn<Stock, Integer> quantityCol;
    @FXML
    private TableView<Stock> stockTable;
    @FXML
    private AnchorPane stockDetailsPane;
    @FXML
    private Label stockNameLabel, stockTypeLabel, quantityLabel, criticalLevelLabel, unitMeasureLabel;
    @FXML
    protected TextField searchField;

    private Stock selectedStock;

    private ViewStockInventoryModel model;

    @FXML
    public void initialize() {
        this.stockDetailsPane.setVisible(false);
        this.model = new ViewStockInventoryModel(this);
        initializeTable();
        configureOnClick();
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

    // Initalize table
    private void initializeTable() {
        quantityCol.setCellValueFactory(new PropertyValueFactory<Stock, Integer>("quantity"));
        stockNameCol.setCellValueFactory(new PropertyValueFactory<Stock, String>("stock_name"));
        stockTypeCol.setCellValueFactory(new PropertyValueFactory<Stock, String>("stock_type_id_string"));
        unitMeasureCol.setCellValueFactory(new PropertyValueFactory<Stock, String>("unit_measure_id_string"));

        // Disable column reodering
        stockTable.getColumns().forEach(e -> {
            e.setReorderable(false);
        });
    }

    // Retrieves stocks from database
    public void retrieveStocks(String stockName) {
        try {
            this.stockDetailsPane.setVisible(false);
            // Show loading Screen
            this.borderPaneRootSwitcher.showLoadingScreen_BP();

            // Create thread
            Service<Void> userRetriever = new Service<Void>() {
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
            userRetriever.setOnSucceeded(e -> {
                // Exit Loading Screen
                borderPaneRootSwitcher.exitLoadingScreen_BP();
            });
            userRetriever.start();
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
        }
    }

    @FXML
    public void goBack(ActionEvent e) {
        this.borderPaneRootSwitcher.goBack_BP();
    }

    @FXML
    public void editStock(ActionEvent e) {
        System.out.println("editing stock...");
        EditStockInventoryController controller = (EditStockInventoryController) this
                .initializePopUpDialog(ScreenPaths.Paths.EDIT_STOCK.getPath(), loggedInUserInfo);
        // Passing references
        controller.initialize(this.selectedStock, this);
    }

    @FXML
    public void search(ActionEvent e) {
        System.out.println("searching...");
        stockDetailsPane.setVisible(false);
        retrieveStocks(searchField.getText().isBlank() ? null : searchField.getText());
    }

}

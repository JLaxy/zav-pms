package controllers.inventory;

import controllers.ParentController;
import controllers.reusables.SetDecreaseQuantityController;
import enums.ScreenPaths;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import models.helpers.DateHelper;
import models.helpers.PopupDialog;
import models.helpers.UIElementsBuilderHelper;
import models.inventory.SelectStockDecreaseModel;
import models.schemas.PurchasedInventoryItem;

public class SelectStockDecreaseController extends ParentController {

    @FXML
    private TableView<PurchasedInventoryItem> stockTable;
    @FXML
    private TableColumn<PurchasedInventoryItem, String> stockNameCol, datePurchasedCol, expiryDateCol;
    @FXML
    private TableColumn<PurchasedInventoryItem, Double> quantityCol;
    @FXML
    private Label stockNameLabel, quantityLabel, datePurchasedLabel, expiryDateLabel;
    @FXML
    private AnchorPane stockDetailsPane;
    @FXML
    private Button decreaseStockButton;

    private ObservableList<PurchasedInventoryItem> stockPurchases;
    private SelectStockDecreaseModel model;
    private PurchasedInventoryItem selectedPurchasedInventoryItem;
    private int selectedStockID;
    private ViewStockInventoryController viewStockInventoryController;

    @FXML
    public void initialize(int selectedStockID, ViewStockInventoryController viewStockInventoryController) {
        this.viewStockInventoryController = viewStockInventoryController;
        this.stockDetailsPane.setVisible(false);
        this.decreaseStockButton.setVisible(false);
        this.model = new SelectStockDecreaseModel(this);
        this.selectedStockID = selectedStockID;
        configureTableView();
        retrieveStockPurchases();
        configureOnClick();
    }

    private void configureOnClick() {
        this.stockTable.getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<PurchasedInventoryItem>() {
                    @Override
                    public void changed(ObservableValue<? extends PurchasedInventoryItem> arg0,
                            PurchasedInventoryItem arg1,
                            PurchasedInventoryItem arg2) {
                        if (stockTable.getSelectionModel().getSelectedItem() != null) {
                            selectedPurchasedInventoryItem = stockTable.getSelectionModel().getSelectedItem();

                            stockNameLabel.setText(selectedPurchasedInventoryItem.getInventory_item_name());
                            quantityLabel.setText(String.valueOf(selectedPurchasedInventoryItem.getQuantity()));
                            datePurchasedLabel.setText(selectedPurchasedInventoryItem.getDate_purchased());
                            expiryDateLabel.setText(selectedPurchasedInventoryItem.getExpiry_date());

                            // If is already expired
                            if (DateHelper.isDateBeforeNow(
                                    DateHelper.stringToDate(selectedPurchasedInventoryItem.getExpiry_date()))
                                    || DateHelper.isDateNow(
                                            DateHelper.stringToDate(selectedPurchasedInventoryItem.getExpiry_date()))) {
                                UIElementsBuilderHelper.applyRedToText(expiryDateLabel);
                            } else if (DateHelper.isDateOnNext7Days(
                                    DateHelper.stringToDate(selectedPurchasedInventoryItem.getExpiry_date()))) {
                                UIElementsBuilderHelper.applyYellowToText(expiryDateLabel);
                            } else
                                UIElementsBuilderHelper.applyBlackToText(expiryDateLabel);

                            stockDetailsPane.setVisible(true);
                            decreaseStockButton.setVisible(true);
                        }
                    }
                });

    }

    private void configureTableView() {
        quantityCol.setCellValueFactory(new PropertyValueFactory<PurchasedInventoryItem, Double>("quantity"));
        stockNameCol
                .setCellValueFactory(new PropertyValueFactory<PurchasedInventoryItem, String>("inventory_item_name"));
        datePurchasedCol
                .setCellValueFactory(new PropertyValueFactory<PurchasedInventoryItem, String>("date_purchased"));
        expiryDateCol
                .setCellValueFactory(new PropertyValueFactory<PurchasedInventoryItem, String>("expiry_date"));

        stockTable.getColumns().forEach(e -> {
            e.setReorderable(false);
        });
    }

    private void retrieveStockPurchases() {
        Task<Void> stockPurchasesRetriever = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                stockPurchases = model.getInventoryItemPurchases(selectedStockID);

                Platform.runLater(() -> stockTable.setItems(stockPurchases));

                return null;
            }
        };

        stockPurchasesRetriever.setOnRunning(e -> {
            this.stockDetailsPane.setVisible(false);
            this.borderPaneRootSwitcher.showLoadingScreen_BP();
        });
        stockPurchasesRetriever.setOnSucceeded(e -> this.borderPaneRootSwitcher.exitLoadingScreen_BP());

        Thread myThread = new Thread(stockPurchasesRetriever);
        myThread.setDaemon(true);
        myThread.start();
    }

    @FXML
    private void decreaseStock(ActionEvent e) {
        SetDecreaseQuantityController controller = (SetDecreaseQuantityController) this
                .initializePopUpDialog(ScreenPaths.Paths.SET_DECREASE_QUANTITY.getPath(), loggedInUserInfo);
        controller.initialize(this, selectedPurchasedInventoryItem.getQuantity());
    }

    public void confirmDecrease(double decreaseQuantity, String reductionType) {

        if (!this.model.confirmDecrease(selectedPurchasedInventoryItem,
                decreaseQuantity, reductionType,
                loggedInUserInfo)) {
            PopupDialog.showCustomErrorDialog("An error has occured!");
        }

        PopupDialog.showInfoDialog("Stock Reduced", "Successfully reduced stock!");

        retrieveStockPurchases();
    }

    @FXML
    private void goBack(ActionEvent e) {
        this.borderPaneRootSwitcher.goBack_BP();
        this.viewStockInventoryController.retrieveStocks(null);
    }
}

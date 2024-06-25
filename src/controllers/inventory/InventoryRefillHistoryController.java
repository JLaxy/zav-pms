package controllers.inventory;

import controllers.ParentController;
import enums.StockProductType;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import models.helpers.DateHelper;
import models.helpers.PopupDialog;
import models.helpers.NumberHelper;
import models.inventory.InventoryRefillHistoryModel;
import models.schemas.PurchasedInventoryItem;

public class InventoryRefillHistoryController extends ParentController {

    // Contains all of purchased items in database
    private ObservableList<PurchasedInventoryItem> purchasedItems;
    private InventoryRefillHistoryModel model;
    // Used to divide list of purchased items by ITEMS_PER_PAGE
    private int pageNo, ITEMS_PER_PAGE = 10;

    @FXML
    private TextField searchField;
    @FXML
    private Button leftArrowButton, rightArrowButton;
    @FXML
    private TableView<PurchasedInventoryItem> inventoryHistoryTable;
    @FXML
    private TableColumn<PurchasedInventoryItem, String> datePurchaseCol, inventoryItemCol;
    @FXML
    private VBox refillHistoryDetailsBox;
    @FXML
    private Label pageNoLabel, inventoryItemLabel, quantityLabel, totalCostLabel, datePurchasedLabel, expiryDateLabel,
            unitMeasureLabel, sizeLabel;

    @FXML
    private void initialize() {
        this.model = new InventoryRefillHistoryModel(this);
        this.configureTableView();
        this.configureOnClick();
        // Hiding details box
        this.refillHistoryDetailsBox.setVisible(false);
    }

    private void configureOnClick() {
        this.inventoryHistoryTable.getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<PurchasedInventoryItem>() {
                    @Override
                    public void changed(ObservableValue<? extends PurchasedInventoryItem> arg0,
                            PurchasedInventoryItem arg1,
                            PurchasedInventoryItem arg2) {
                        PurchasedInventoryItem selectedItem = inventoryHistoryTable.getSelectionModel()
                                .getSelectedItem();
                        // If selected, update labels
                        if (selectedItem != null) {
                            refillHistoryDetailsBox.setVisible(true);
                            inventoryItemLabel.setText(selectedItem.getInventory_item_name());
                            quantityLabel.setText(String.valueOf(selectedItem.getQuantity()));
                            totalCostLabel.setText(NumberHelper.toTwoDecimalPlaces(selectedItem.getTotal_cost()));
                            datePurchasedLabel.setText(DateHelper
                                    .dateToFormattedDate(DateHelper.stringToDate(selectedItem.getDate_purchased())));
                            expiryDateLabel.setText(DateHelper
                                    .dateToFormattedDate(DateHelper.stringToDate(selectedItem.getExpiry_date())));
                            unitMeasureLabel.setText(selectedItem.getUnit_measure());
                            sizeLabel.setText(String.valueOf(selectedItem.getSize()));
                            sizeLabel.setVisible(true);

                            // If stock, hide labels
                            if (selectedItem.getStock_product_type_id() != StockProductType.Type.BEVERAGE.getValue()) {
                                sizeLabel.setVisible(false);
                            }
                        }
                    }
                });
    }

    // Configures columns of tableview
    private void configureTableView() {
        datePurchaseCol.setCellValueFactory(new PropertyValueFactory<PurchasedInventoryItem, String>("date_purchased"));
        inventoryItemCol
                .setCellValueFactory(new PropertyValueFactory<PurchasedInventoryItem, String>("inventory_item_name"));

        // Disable column reodering
        inventoryHistoryTable.getColumns().forEach(e -> {
            e.setReorderable(false);
        });
    }

    // Retrieves inventory history from database
    public void retrieveInventoryHistory(String userQuery) {
        try {
            // Show loading Screen
            this.borderPaneRootSwitcher.showLoadingScreen_BP();

            // Retrieves inventory history from database
            Service<Void> inventoryHistoryRetriever = new Service<Void>() {
                @Override
                protected Task<Void> createTask() {
                    return new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            purchasedItems = model.getInventoryPurchaseHistory(userQuery);

                            // Update UI
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    updateTableView(true);
                                }
                            });

                            return null;
                        }
                    };
                }
            };

            inventoryHistoryRetriever.setOnSucceeded(e -> {
                // Exit Loading Screen
                borderPaneRootSwitcher.exitLoadingScreen_BP();
            });

            inventoryHistoryRetriever.start();
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
        }
    }

    // Fixes information to be shown on user depending on page
    private void updateTableView(boolean didResultUpdate) {
        ObservableList<PurchasedInventoryItem> displayedItems = FXCollections.observableArrayList();

        if (didResultUpdate)
            // Sets initial page to 1
            this.pageNo = 1;

        // Index limit of items will be displayed
        int indexLimit = this.ITEMS_PER_PAGE * this.pageNo;
        // Starting index of loop
        int start = indexLimit - this.ITEMS_PER_PAGE;

        // Try to catch if at end of result
        try {
            // Iterate then add to obeservable list that will be displayed
            for (; start < indexLimit; start++)
                displayedItems.add(this.purchasedItems.get(start));
        } catch (Exception e) {
            System.out.println("last result at index number " + start);
        }

        this.inventoryHistoryTable.setItems(displayedItems);
        this.updateNavigationState();
    }

    // Go back to previous page
    @FXML
    private void goBack() {
        this.borderPaneRootSwitcher.goBack_BP();
    }

    @FXML
    private void search() {
        retrieveInventoryHistory(searchField.getText());
        this.refillHistoryDetailsBox.setVisible(false);
    }

    // Change table view page
    @FXML
    private void changePage(ActionEvent e) {
        Button calledButton = new Button();

        try {
            calledButton = (Button) e.getSource();
        } catch (Exception ex) {
            // Safety catch
        }

        if (calledButton == this.leftArrowButton)
            pageNo -= 1;
        else if (calledButton == this.rightArrowButton)
            pageNo += 1;

        this.updateTableView(false);
        this.updateNavigationState();
    }

    // Updates states of buttons
    private void updateNavigationState() {
        this.pageNoLabel.setText(String.valueOf(this.pageNo));

        this.leftArrowButton.setDisable(false);
        this.rightArrowButton.setDisable(false);

        // If can still display more, switch button
        if (this.pageNo * this.ITEMS_PER_PAGE > this.purchasedItems.size())
            this.rightArrowButton.setDisable(true);

        if (this.pageNo == 1)
            this.leftArrowButton.setDisable(true);
    }
}

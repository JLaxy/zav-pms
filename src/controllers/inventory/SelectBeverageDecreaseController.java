package controllers.inventory;

import controllers.ParentController;
import controllers.reusables.SetBeverageDecreaseQuantityController;
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
import models.inventory.SelectBeverageDecreaseModel;
import models.schemas.DrinkVariant;
import models.schemas.PurchasedInventoryItem;

public class SelectBeverageDecreaseController extends ParentController {

    @FXML
    private TableView<PurchasedInventoryItem> beverageTable;
    @FXML
    private TableColumn<PurchasedInventoryItem, String> beverageNameCol, datePurchasedCol, expiryDateCol;
    @FXML
    private TableColumn<PurchasedInventoryItem, Integer> quantityCol;
    @FXML
    private AnchorPane beverageDetailsPane;
    @FXML
    private Label beverageNameLabel, quantityLabel, datePurchasedLabel, expiryDateLabel;
    @FXML
    private Button decreaseBeverageButton;

    private ViewBeverageProductController viewBeverageProductController;
    private SelectBeverageDecreaseModel model;
    private DrinkVariant selectedDrink;
    private PurchasedInventoryItem selectedBeveragePurchase;
    private ObservableList<PurchasedInventoryItem> itemPurchases;

    @FXML
    public void initialize(DrinkVariant selectedDrink, ViewBeverageProductController viewBeverageProductController) {
        this.beverageDetailsPane.setVisible(false);
        this.decreaseBeverageButton.setVisible(false);
        this.model = new SelectBeverageDecreaseModel(this);
        this.selectedDrink = selectedDrink;
        this.viewBeverageProductController = viewBeverageProductController;
        retrievePurchases();
        configureTable();
        configureOnClick();
    }

    private void configureTable() {
        quantityCol.setCellValueFactory(new PropertyValueFactory<PurchasedInventoryItem, Integer>("quantity"));
        beverageNameCol
                .setCellValueFactory(new PropertyValueFactory<PurchasedInventoryItem, String>("inventory_item_name"));
        datePurchasedCol
                .setCellValueFactory(new PropertyValueFactory<PurchasedInventoryItem, String>("date_purchased"));
        expiryDateCol
                .setCellValueFactory(new PropertyValueFactory<PurchasedInventoryItem, String>("expiry_date"));

        beverageTable.getColumns().forEach(e -> {
            e.setReorderable(false);
        });
    }

    private void configureOnClick() {
        this.beverageTable.getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<PurchasedInventoryItem>() {
                    @Override
                    public void changed(ObservableValue<? extends PurchasedInventoryItem> arg0,
                            PurchasedInventoryItem arg1,
                            PurchasedInventoryItem arg2) {
                        if (beverageTable.getSelectionModel().getSelectedItem() != null) {
                            selectedBeveragePurchase = beverageTable.getSelectionModel().getSelectedItem();

                            beverageNameLabel.setText(selectedBeveragePurchase.getInventory_item_name());
                            quantityLabel.setText(String.valueOf(selectedBeveragePurchase.getQuantity()));
                            datePurchasedLabel.setText(selectedBeveragePurchase.getDate_purchased());
                            expiryDateLabel.setText(selectedBeveragePurchase.getExpiry_date());

                            // If is already expired
                            if (DateHelper.isDateBeforeNow(
                                    DateHelper.stringToDate(selectedBeveragePurchase.getExpiry_date()))
                                    || DateHelper.isDateNow(
                                            DateHelper.stringToDate(selectedBeveragePurchase.getExpiry_date()))) {
                                UIElementsBuilderHelper.applyRedToText(expiryDateLabel);
                            } else if (DateHelper.isDateOnNext7Days(
                                    DateHelper.stringToDate(selectedBeveragePurchase.getExpiry_date()))) {
                                UIElementsBuilderHelper.applyYellowToText(expiryDateLabel);
                            } else
                                UIElementsBuilderHelper.applyBlackToText(expiryDateLabel);

                            beverageDetailsPane.setVisible(true);
                            decreaseBeverageButton.setVisible(true);
                        }
                    }
                });

    }

    // Retrieves purchases from database
    private void retrievePurchases() {
        Task<Void> purchasesRetriever = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    itemPurchases = model.retrieveItemPurchases(selectedDrink);
                    Platform.runLater(() -> beverageTable.setItems(itemPurchases));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        purchasesRetriever.setOnRunning(e -> this.borderPaneRootSwitcher.showLoadingScreen_BP());
        purchasesRetriever.setOnSucceeded(e -> {
            this.beverageDetailsPane.setVisible(false);
            this.borderPaneRootSwitcher.exitLoadingScreen_BP();
        });

        Thread myThread = new Thread(purchasesRetriever);
        myThread.setDaemon(true);
        myThread.start();
    }

    @FXML
    private void decreaseBeverage(ActionEvent e) {
        SetBeverageDecreaseQuantityController controller = (SetBeverageDecreaseQuantityController) this
                .initializePopUpDialog(ScreenPaths.Paths.SET_BEVERAGE_DECREASE_QUANTITY.getPath(), loggedInUserInfo);
        controller.initialize(this, this.selectedBeveragePurchase.getQuantity());
    }

    public void confirmDecrease(int decreaseQuantity, String reductionType) {

        if (!this.model.confirmDecrease(selectedBeveragePurchase,
                decreaseQuantity, reductionType,
                loggedInUserInfo)) {
            PopupDialog.showCustomErrorDialog("An error has occured!");
        }

        PopupDialog.showInfoDialog("Beverage Reduced", "Successfully reduced beverage!");

        retrievePurchases();
    }

    @FXML
    private void goBack(ActionEvent e) {
        this.borderPaneRootSwitcher.goBack_BP();
        this.viewBeverageProductController.retrieveBeverageProducts();
    }
}

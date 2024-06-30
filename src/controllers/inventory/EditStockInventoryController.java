package controllers.inventory;

import javax.swing.JOptionPane;

import controllers.ParentController;
import enums.ScreenPaths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import models.helpers.PopupDialog;
import models.inventory.EditStockInventoryModel;
import models.schemas.Stock;

public class EditStockInventoryController extends ParentController {

    private Stock selectedStock;
    private EditStockInventoryModel model;
    private ViewStockInventoryController viewStockInventoryController;

    @FXML
    public void initialize(Stock selectedStock, ViewStockInventoryController viewStockInventoryController) {
        this.model = new EditStockInventoryModel(this);
        this.selectedStock = selectedStock;
        this.viewStockInventoryController = viewStockInventoryController;
    }

    @FXML
    private void increaseStock(ActionEvent e) {
        IncrementStockInventoryController controller = (IncrementStockInventoryController) this
                .initializePopUpDialog(ScreenPaths.Paths.INCREMENT_STOCK_INVENTORY.getPath(), this.loggedInUserInfo);
        controller.initialize(this.selectedStock, this.viewStockInventoryController);
    }

    @FXML
    private void decreaseStock(ActionEvent e) {
        System.out.println("decreasing quantity of stock with id: " + this.selectedStock.getId());
        this.borderPaneRootSwitcher.exitPopUpDialog();
        SelectStockDecreaseController controller = (SelectStockDecreaseController) this.initializeNextScreen_BP(
                ScreenPaths.Paths.SELECT_STOCK_DECREASE.getPath(), this.loggedInUserInfo,
                "SELECT STOCK");
        controller.initialize(this.selectedStock.getId());
    }

    // Void currently selected stock
    @FXML
    private void voidStock(ActionEvent e) {
        System.out.println("voiding stock...");

        // Show confirmation dialog
        if (PopupDialog.confirmOperationDialog("Do you really want to void this stock?") != JOptionPane.YES_OPTION)
            return;

        // Get copy of stock
        Stock voidedStock = this.selectedStock.getCopy();
        voidedStock.toggleVoidStatus();

        // If failed
        if (!this.model.editStock(voidedStock, selectedStock, loggedInUserInfo)) {
            PopupDialog.showCustomErrorDialog("Failed to void stock!");
            return;
        }

        PopupDialog.showInfoDialog("Voided Stock",
                "Successfully voided stock \"" + voidedStock.getStock_name() + "\"");

        // Exit pop up dialog
        this.borderPaneRootSwitcher.exitPopUpDialog();
        // Retrieve stocks from database
        this.viewStockInventoryController
                .retrieveStocks(this.viewStockInventoryController.searchField.getText().isBlank() ? null
                        : this.viewStockInventoryController.searchField.getText());
    }

    @FXML
    private void cancel(ActionEvent e) {
        this.borderPaneRootSwitcher.exitPopUpDialog();
    }
}

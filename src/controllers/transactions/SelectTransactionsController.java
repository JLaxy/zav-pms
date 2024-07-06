package controllers.transactions;

import javax.swing.JOptionPane;

import controllers.ParentController;
import enums.ScreenPaths;
import enums.StockProductType;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import models.helpers.PopupDialog;
import models.schemas.Transaction;

public class SelectTransactionsController extends ParentController {

    // Defines what inventory item will be searched in transaction
    private StockProductType.Type inventoryItemType;

    // List of transactions retrieved from database
    private ObservableList<Transaction> transactionsList;

    @FXML
    public void initialize(StockProductType.Type inventoryItemType) {
        this.inventoryItemType = inventoryItemType;
        this.retrieveTransactions();
    }

    private void retrieveTransactions() {
        // retrieve transaction from database using SelectTransactionsModel (create it)
        // then pass
        // controller instance; access Database on model by
        // "this.controller.getDBManager().query.FUNCTION"
    }

    @FXML
    private void goBack(ActionEvent e) {
        if (PopupDialog.confirmOperationDialog("Do you want to cancel this operation?") != JOptionPane.YES_OPTION) {
            return;
        }

        this.borderPaneRootSwitcher.goBack_BP();
    }

    @FXML
    private void search(ActionEvent e) {
        // retrieve transaction of specific customer name... (use retrieveTransactions
        // function and just modify query, check syncUserTableView of
        // ManageAccountsController under src/controllers/manageaccounts)
    }

    @FXML
    private void selectTransaction() {
        // Select transaction using its ID then pass transaction to next screeen to
        // retrieve the
        // orders of that transaction

        // If food orders will be retrieved
        if (inventoryItemType == StockProductType.Type.FOOD) {
            this.initializeNextScreen_BP(ScreenPaths.Paths.SELECT_FOOD_ORDER.getPath(), loggedInUserInfo,
                    "SELECT FOOD ORDER");
            // Else if beverage
        } else if (inventoryItemType == StockProductType.Type.BEVERAGE) {
            this.initializeNextScreen_BP(ScreenPaths.Paths.SELECT_BEVERAGE_ORDER.getPath(), loggedInUserInfo,
                    "SELECT BEVERAGE ORDER");
        }
    }
}

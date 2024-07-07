package controllers.transactions;

import java.time.LocalDateTime;

import javax.swing.JOptionPane;

import controllers.ParentController;
import enums.ScreenPaths;
import enums.StockProductType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import models.helpers.PopupDialog;
import models.schemas.DiscountCard;
import models.schemas.OrderProduct;
import models.schemas.Transaction;
import models.transactions.SelectTransactionsModel;

public class SelectTransactionsController extends ParentController {

    // Defines what inventory item will be searched in transaction
    private StockProductType.Type inventoryItemType;
    private SelectTransactionsModel model;

    // List of transactions retrieved from database
    private ObservableList<Transaction> transactionsList;
    @FXML
    private TextField search;
    


    @FXML
    public void initialize(StockProductType.Type inventoryItemType) {
    	this.model = new SelectTransactionsModel(this);
        this.inventoryItemType = inventoryItemType;
        this.retrieveTransactions();
        
    }

    private void retrieveTransactions() {
        transactionsList = model.retrieveTransactions(null);
    }

    @FXML
    private void goBack(ActionEvent e) {
        if (PopupDialog.confirmOperationDialog("Do you want to cancel this operation?") != JOptionPane.YES_OPTION) {
            return;
        }

        this.borderPaneRootSwitcher.goBack_BP();
    }

    @FXML
    private void search(ActionEvent actionEvent) {
        // retrieve transaction of specific customer name... (use retrieveTransactions
        // function and just modify query, check syncUserTableView of
        // ManageAccountsController under src/controllers/manageaccounts)
    	try {
            this.transactionsList = FXCollections.observableArrayList();
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
                        	transactionsList = model.retrieveTransactions(search.getText() == null ? null
                                    : (search.getText().isBlank() ? null : search.getText()));
                            // Update table view with list of users where password is muted
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
        } catch (Exception exception) {
            PopupDialog.showErrorDialog(exception, this.getClass().getName());
        }
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

package controllers.transactions;

import java.time.LocalDateTime;

import controllers.ParentController;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import models.helpers.DateHelper;
import models.helpers.NumberHelper;
import models.helpers.database.DBManager;
import models.schemas.Transaction;
import models.transactions.ViewTransactionsModel;

public class ViewTransactionsController extends ParentController {
	@FXML
	private TableView<Transaction> transactionTable;
	@FXML
	private TableColumn<Transaction, String> transactionTypeCol;
	@FXML
	private TableColumn<Transaction, String> customerNameCol;
	@FXML
	private TableColumn<Transaction, LocalDateTime> orderDateCol;
	@FXML
	private TableColumn<Transaction, LocalDateTime> targetDateCol;
	@FXML
	private Button searchButton, editTransactionButton;
	@FXML
	private TextField searchField;
	@FXML
	private Label customerNameLabel, statusLabel, contactNumLabel, transactionTypeLabel, orderDateLabel,
			targetDateLabel, fulfillmentDateLabel, amountPayableLabel, balanceLabel;
	@FXML
	private ScrollPane transactionDetailsScrollPane;

	private ViewTransactionsModel model;
	private ObservableList<Transaction> transactions;
	private Transaction selectedTransaction;

	@FXML
	private void initialize() {
		zavPMSDB = new DBManager();
		model = new ViewTransactionsModel(this);
		this.search();
		this.configureTable();
		this.configureOnClick();
	}

	@FXML
	private void goBack(ActionEvent e) {
		this.borderPaneRootSwitcher.goBack_BP();
	}

	@FXML
	private void configureTable() {
		customerNameCol.setCellValueFactory(new PropertyValueFactory<Transaction, String>("customer_name"));
		transactionTypeCol.setCellValueFactory(new PropertyValueFactory<Transaction, String>("transaction_type"));
		orderDateCol.setCellValueFactory(new PropertyValueFactory<Transaction, LocalDateTime>("order_date"));
		targetDateCol.setCellValueFactory(new PropertyValueFactory<Transaction, LocalDateTime>("target_date"));

	}

	private void configureOnClick() {
		this.transactionTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Transaction>() {
			@Override
			public void changed(ObservableValue<? extends Transaction> arg0, Transaction arg1, Transaction arg2) {
				// If has selected an item
				if (transactionTable.getSelectionModel().getSelectedItem() != null) {
					selectedTransaction = transactionTable.getSelectionModel().getSelectedItem();

					customerNameLabel.setText(selectedTransaction.getCustomer_name());
					contactNumLabel.setText(selectedTransaction.getContact_number());
					// statusLabel.setText(selectedTransaction.);
					transactionTypeLabel.setText(selectedTransaction.getTransaction_type());
					orderDateLabel.setText(DateHelper.dateTimeToString(selectedTransaction.getOrder_date()));
					targetDateLabel.setText(DateHelper.dateTimeToString(selectedTransaction.getTarget_date()));
					fulfillmentDateLabel.setText(selectedTransaction.getFulfillment_date() == null ? ""
							: DateHelper.dateTimeToString(selectedTransaction.getFulfillment_date()));
					amountPayableLabel
							.setText(NumberHelper.toTwoDecimalPlaces(selectedTransaction.getTotal_amount_payable()));
					balanceLabel
							.setText(NumberHelper.toTwoDecimalPlaces(selectedTransaction.getBalance()));

					transactionDetailsScrollPane.setVisible(true);
					editTransactionButton.setVisible(true);
				}
			}
		});
	}

	@FXML
	private void search() {
		Task<Void> transRetriever = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				transactions = model
						.getTransactionsByName(searchField.getText().isBlank() ? null : searchField.getText());
				Platform.runLater(() -> transactionTable.setItems(transactions));
				return null;
			}
		};

		transRetriever.setOnRunning(e -> {
			this.transactionDetailsScrollPane.setVisible(false);
			this.editTransactionButton.setVisible(false);
			this.borderPaneRootSwitcher.showLoadingScreen_BP();
		});
		transRetriever.setOnSucceeded(e -> this.borderPaneRootSwitcher.exitLoadingScreen_BP());

		Thread myThread = new Thread(transRetriever);
		myThread.setDaemon(true);
		myThread.start();
	}

	@FXML
	private void editTransaction(ActionEvent e) {
		System.out.println("edit trans");
	}
}

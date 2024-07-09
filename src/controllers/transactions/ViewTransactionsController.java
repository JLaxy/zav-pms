package controllers.transactions;

import java.time.LocalDateTime;

import controllers.ParentController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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
	private Button searchButton;
	@FXML
	private TextField searchField;
	@FXML
	private Label customerNameLabel, statusLabel, contactNumLabel, transactionTypeLabel, orderDateLabel,
			targetDateLabel, fulfillmentDateLabel, amountPayableLabel, totalDueLabel;

	private ViewTransactionsModel model;
	private ObservableList<Transaction> transactions;
	private Transaction selectedTransaction;

	@FXML
	private void initialize() {
		zavPMSDB = new DBManager();
		model = new ViewTransactionsModel(this);
		transactions = model.getTransactions();
		this.configureTable();
	}

	@FXML
	private void goBack(ActionEvent e) {
		this.borderPaneRootSwitcher.goBack_BP();
	}

	@FXML
	private void configureTable() {
		customerNameCol.setCellValueFactory(new PropertyValueFactory<Transaction, String>("customer_name"));
		for (Transaction t : transactions) {
			if (t.getTransaction_type_id() == 0) {
				t.setTransaction_type("delivery");
			} else if (t.getTransaction_type_id() == 1) {
				t.setTransaction_type("pick up");
			} else {
				t.setTransaction_type("reservation");
			}
			System.out.println(t.getTransaction_type());
			transactions.set(transactions.indexOf(t), t);
		}
		transactionTypeCol.setCellValueFactory(new PropertyValueFactory<Transaction, String>("transaction_type"));
		orderDateCol.setCellValueFactory(new PropertyValueFactory<Transaction, LocalDateTime>("order_date"));
		targetDateCol.setCellValueFactory(new PropertyValueFactory<Transaction, LocalDateTime>("target_date"));
		transactionTable.setItems(transactions);

	}

	private void configureOnClick(){
	this.transactionTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Transaction>() {
		@Override
		public void changed(ObservableValue<? extends Transaction> arg0, Transaction arg1, Transaction arg2) {
			// If has selected an item
			if (transactionTable.getSelectionModel().getSelectedItem() != null) {
				selectedTransaction = transactionTable.getSelectionModel().getSelectedItem();

				customerNameLabel.setText(selectedTransaction.getCustomer_name());
				contactNumLabel.setText(selectedTransaction.getContact_number());
				// statusLabel.setText(selectedTransaction.);
			}
		}
	});
	}

	@FXML
	private void search(ActionEvent e) {
		EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				ObservableList<Transaction> transactionsFiltered = model.getTransactionsByName(searchField.getText());
				for (Transaction t : transactionsFiltered) {
					if (t.getTransaction_type_id() == 0) {
						t.setTransaction_type("delivery");
					} else if (t.getTransaction_type_id() == 1) {
						t.setTransaction_type("pick up");
					} else {
						t.setTransaction_type("reservation");
					}
					transactionsFiltered.set(transactionsFiltered.indexOf(t), t);
				}
				if (searchField.getText().equals("")) {
					transactionTable.setItems(transactions);
				} else {
					transactionTable.setItems(transactionsFiltered);

				}

			}
		};
		searchButton.setOnAction(event);
	}

	@FXML
	private void editTransaction(ActionEvent e) {
		System.out.println("edit trans");
	}
}

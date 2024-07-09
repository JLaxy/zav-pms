package controllers.order;

import java.time.LocalDateTime;

import controllers.ParentController;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import models.helpers.database.DBManager;
import models.order.ViewBackOrderModel;
import models.schemas.OrderProduct;
import models.schemas.OrderedProduct;
import models.schemas.Transaction;
import models.transactions.ViewTransactionsModel;

public class ViewBackOrderController extends ParentController{
	 @FXML
	    private TableView<Transaction> stockTable;
	    @FXML
	    private TableColumn<Transaction, String> stockNameCol;
	    @FXML
	    private TableColumn<Transaction, String> stockTypeCol;
	    @FXML
	    private TableColumn<Transaction, Integer> quantityCol;
	    @FXML
	    private TableColumn<Transaction, Double> unitMeasureCol;
	    @FXML
	    private TextField searchField;
	    @FXML
	    private Button searchButton;
	    private ObservableList<Transaction> backOrderedTransactions;
	    private ViewBackOrderModel model;
	    @FXML
	    private void initialize() {
	    	zavPMSDB = new DBManager();
	    	model= new ViewBackOrderModel(this);
	    	backOrderedTransactions = model.getBackOrder();
	        this.configureTable();
	    }
	    
	    @FXML
	    private void configureTable() {
	    	stockNameCol.setCellValueFactory(new PropertyValueFactory<Transaction, String>("id"));
	    	stockTypeCol.setCellValueFactory(new PropertyValueFactory<Transaction, String>("customer_name"));
	    	quantityCol.setCellValueFactory(new PropertyValueFactory<Transaction, Integer>("order_date"));
	    	unitMeasureCol.setCellValueFactory(new PropertyValueFactory<Transaction, Double>("target_date"));
	    	stockTable.setItems(backOrderedTransactions);
	    	
	    }
	    
	    @FXML
	    private void goBack(ActionEvent event) {
	        System.out.println("going back...");
	        this.borderPaneRootSwitcher.goBack_BP();
	    }
	    @FXML
	    private void search(ActionEvent e) {
	    	EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() { 
	            public void handle(ActionEvent e) 
	            { 
	            	ObservableList<Transaction> transactionsFiltered = model.getTransactionsByName(searchField.getText());
	            	for(Transaction t : transactionsFiltered) {
	            		if(t.getTransaction_type_id()==0) {
	            	    	t.setTransaction_type("delivery");
	            		}else if(t.getTransaction_type_id()==1) {
	            	    	t.setTransaction_type("pick up");
	            		}else {
	            	    	t.setTransaction_type("reservation");
	            		}
	            		transactionsFiltered.set(transactionsFiltered.indexOf(t), t);
	            	}
	            	if(searchField.getText().equals("")) {
	            		stockTable.setItems(transactionsFiltered);
	            	}else {
	            		stockTable.setItems(transactionsFiltered);

	            	}

	            } 
	        }; 
	        searchButton.setOnAction(event);
	    }
}

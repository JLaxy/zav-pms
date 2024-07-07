package controllers.order;

import java.time.LocalDateTime;

import controllers.ParentController;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import models.helpers.database.DBManager;
import models.schemas.OrderProduct;
import models.schemas.OrderedProduct;
import models.schemas.Transaction;
import models.transactions.ViewTransactionsModel;

public class ViewOrderController  extends ParentController{
	@FXML
    private Button searchButton;
    
    @FXML
    private TextField searchField;
	 @FXML
	    private TableView<Transaction> stockTable;
	    @FXML
	    private TableColumn<Transaction, String> stockNameCol;
	    @FXML
	    private TableColumn<Transaction, String> stockTypeCol;
	    @FXML
	    private TableColumn<Transaction, LocalDateTime> quantityCol;
	    @FXML
	    private TableColumn<Transaction, LocalDateTime> unitMeasureCol;
	    @FXML
	    private Label customerNameLabel,transactionTypeLabel,targetDateLabel,quantityLabel,criticalLevelLabel;
	    
	    private ObservableList<Transaction> transactions;
	    private ObservableList<OrderedProduct> orders;
	    private Transaction transaction;

	    @FXML
	    private void initialize() {
	    	zavPMSDB = new DBManager();
	    	transactions=zavPMSDB.query.getTransactions(null);
	    	orders = zavPMSDB.query.getOrder();
	    	for(Transaction t : transactions) {
	    		if(t.getTransaction_type_id()==0) {
	    	    	t.setTransaction_type("delivery");
	    		}else if(t.getTransaction_type_id()==1) {
	    	    	t.setTransaction_type("pick up");
	    		}else {
	    	    	t.setTransaction_type("reservation");
	    		}
	        	System.out.println(t.getTransaction_type());
	        	transactions.set(transactions.indexOf(t), t);
	    	}
	        this.configureTable();
	    }
	    
	    private void configureTable() {
	    	
	    	stockNameCol.setCellValueFactory(new PropertyValueFactory<Transaction, String>("id"));
	    	stockTypeCol.setCellValueFactory(new PropertyValueFactory<Transaction, String>("customer_name"));
	    	quantityCol.setCellValueFactory(new PropertyValueFactory<Transaction, LocalDateTime>("order_date"));
	    	unitMeasureCol.setCellValueFactory(new PropertyValueFactory<Transaction, LocalDateTime>("target_date"));
	    	ObservableList<Transaction> filteredTransactions= FXCollections.observableArrayList();
	    	for(Transaction transaction : transactions) {
	    		for(int i=0; i<orders.size(); i++) {
	    			if(orders.get(i).getTransaction_id()==transaction.getId()) {
	        			filteredTransactions.add(transaction);
	    			}
	    		}
	    	}
	    	stockTable.setItems(filteredTransactions);
	    	EventHandler<MouseEvent> event = new EventHandler<MouseEvent>() { 
	            public void handle(MouseEvent e) 
	            { 
	            	Transaction transaction = stockTable.getSelectionModel().getSelectedItem();
            		for(OrderedProduct order : orders) {
                		if(transaction.getId()==order.getTransaction_id()) {
                			customerNameLabel.setText(transaction.getCustomer_name());
                			transactionTypeLabel.setText(transaction.getTransaction_type());
                			targetDateLabel.setText(transaction.getTarget_date().toString());
                			quantityLabel.setText(order.getCurrent_quantity()+"");
                			criticalLevelLabel.setText(order.getRequired_quantity()+"");
                		}
            	}

	            } 
	        };
	               stockTable.setOnMouseClicked(event);
	               stockTable.onMouseClickedProperty();
	          
	    }
	    
	    @FXML
	    public void goBack(ActionEvent e) {
	        this.borderPaneRootSwitcher.goBack_BP();
	    }
	    
	    @FXML
	    private void search(ActionEvent e) {
	    	EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() { 
	            public void handle(ActionEvent e) 
	            { 
	            	ObservableList<Transaction> transactionsFiltered = zavPMSDB.query.getTransactions(searchField.getText());
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
	            		stockTable.setItems(transactions);
	            	}else {
	            		stockTable.setItems(transactionsFiltered);

	            	}

	            } 
	        }; 
	        searchButton.setOnAction(event);
	    }
}

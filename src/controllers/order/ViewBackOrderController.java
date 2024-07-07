package controllers.order;

import java.time.LocalDateTime;

import controllers.ParentController;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import models.helpers.database.DBManager;
import models.order.ViewBackOrderModel;
import models.schemas.OrderProduct;
import models.schemas.Transaction;
import models.transactions.ViewTransactionsModel;

public class ViewBackOrderController extends ParentController{
	 @FXML
	    private TableView<OrderProduct> stockTable;
	    @FXML
	    private TableColumn<OrderProduct, String> stockNameCol;
	    @FXML
	    private TableColumn<OrderProduct, String> stockTypeCol;
	    @FXML
	    private TableColumn<OrderProduct, Integer> quantityCol;
	    @FXML
	    private TableColumn<OrderProduct, Double> unitMeasureCol;
	    @FXML
	    private TextField searchField;
	    
	    private ViewBackOrderModel model;
	    @FXML
	    private void initialize() {
	    	zavPMSDB = new DBManager();
	    	model= new ViewBackOrderModel(this);
	        this.configureTable();
	    }
	    
	    @FXML
	    private void configureTable() {
	    	stockNameCol.setCellValueFactory(new PropertyValueFactory<OrderProduct, String>("customer_name"));
	    	stockTypeCol.setCellValueFactory(new PropertyValueFactory<OrderProduct, String>("transaction_type"));
	    	quantityCol.setCellValueFactory(new PropertyValueFactory<OrderProduct, Integer>("order_date"));
	    	unitMeasureCol.setCellValueFactory(new PropertyValueFactory<OrderProduct, Double>("target_date"));
//	    	stockTable.setItems(transactions);
	    	
	    }
}

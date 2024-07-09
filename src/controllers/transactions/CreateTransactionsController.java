package controllers.transactions;

import java.time.LocalDateTime;
import java.util.ArrayList;

import controllers.ParentController;
import enums.ScreenPaths;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import models.helpers.PopupDialog;
import models.order.CreateOrderModel;
import models.schemas.OrderProduct;
import models.schemas.OrderedProduct;
import models.schemas.Payment;
import models.schemas.Transaction;
import models.transactions.CreateTransactionsModel;

public class CreateTransactionsController extends ParentController {

    @FXML
    private TextField customerNameField, contactNumberField;
    @FXML
    private ComboBox<String> transactionTypeCBox;
    @FXML
    private DatePicker targetDate;

    @FXML
    private Label totalTransactionLabel, requiredDownpaymentLabel;

    private CreateTransactionsModel model;
    private ObservableList<OrderProduct> listOfOrders;
    private String selectedPaymentType;
    private String modeOfPayment;
    private String change;
    private String amount;

    @FXML
    public void initialize(ObservableList<OrderProduct> orderList) {
        this.model = new CreateTransactionsModel(this);
        this.listOfOrders = orderList;

        // Calculate and display the total transaction cost and required downpayment
        double totalCost = calculateTotalTransactionCost();
        double downpayment = calculateRequiredDownpayment(totalCost);

        totalTransactionLabel.setText(String.format("%.2f", totalCost));
        requiredDownpaymentLabel.setText(String.format("%.2f", downpayment));
        ArrayList<String> transactionTypes = new ArrayList<String>();
        transactionTypes.add("delivery");
        transactionTypes.add("pick up");
        transactionTypes.add("reservation");

        transactionTypeCBox.setItems(FXCollections.observableList(transactionTypes));
    }
    
    

    public String getModeOfPayment() {
		return modeOfPayment;
	}



	public void setModeOfPayment(String modeOfPayment) {
		this.modeOfPayment = modeOfPayment;
	}



	public String getSelectedPaymentType() {
		return selectedPaymentType;
	}



	private double calculateTotalTransactionCost() {
        double totalCost = 0.0;
        for (OrderProduct product : listOfOrders) {
            totalCost += product.getAmount();
        }
        return totalCost;
    }

    private double calculateTotalDiscountAmount() {
        double totalDiscount = 0.0;
        for (OrderProduct product : listOfOrders) {
            totalDiscount += product.getDiscountedPrice();
        }
        return totalDiscount;
    }

    private double calculateRequiredDownpayment(double totalCost) {
        return totalCost * 0.50;
    }

    public void setSelectedPaymentType(String paymentType) {
        this.selectedPaymentType = paymentType;
        System.out.println("Payment type set to: " + paymentType);

    }

    public String getChange() {
        return String.valueOf(change);
    }

    public void setChange(String change) {
        this.change = change;
    }

    public String getAmount() {
        return String.valueOf(amount);
    }


	public void setAmount(String amount) {
		this.amount = amount;
		if(!requiredDownpaymentLabel.getText().equals("")) {
			double downPayment=Double.parseDouble(requiredDownpaymentLabel.getText());
			requiredDownpaymentLabel.setText(downPayment-Double.parseDouble(amount)+"");
		}
		
	}

   

    @FXML
    private void addpayment() {
        AddPaymentController controller = (AddPaymentController) this
                .initializePopUpDialog(ScreenPaths.Paths.ADD_PAYMENT.getPath(), this.loggedInUserInfo);
        controller.initialize(null, this);
        System.out.println("Adding Payment");
    }

    @FXML
    private void save() {
        // Save logic here

    	if(modeOfPayment==null || selectedPaymentType==null || amount==null) {
    		PopupDialog.showCustomErrorDialog("Cannot create transaction without payment");
    	}else {
    		Transaction transaction = new Transaction(0,customerNameField.getText(),LocalDateTime.now(),targetDate.getValue().atStartOfDay(),contactNumberField.getText(),transactionTypeCBox.getItems().indexOf(transactionTypeCBox.getValue()),calculateTotalDiscountAmount(),false,calculateTotalTransactionCost(),null,Double.parseDouble(requiredDownpaymentLabel.getText()));
            this.model.saveTransaction(transaction,  loggedInUserInfo);
            ObservableList <Transaction> transactions=this.getDBManager().query.getTransactions(customerNameField.getText());
            for(OrderProduct order : this.listOfOrders) {
           	 this.getDBManager().query.saveOrder(new OrderedProduct(0,transactions.get(0).getId(),this.getDBManager().query.getProductNameId(order.getProductName()),order.getQuantity(),order.getRemainingQuantity(),this.getDBManager().query.getProductNameId(order.getProductName()),""),this.loggedInUserInfo);
           }
            ObservableList<String> paymentTypes = this.getDBManager().query.getPaymentTypes();
            ObservableList<String> paymentModeTypes = this.getDBManager().query.getModeOfPaymentTypes();
            int id=0;
            int paymentId=0;
            for(String payment : paymentModeTypes) {
            	if(modeOfPayment.equals(payment)) {
            		id=paymentModeTypes.indexOf(payment);
            	}
            }
            
            for(String payment : paymentTypes) {
            	if(selectedPaymentType.equals(payment)) {
            		paymentId=paymentTypes.indexOf(payment)+1;
            	}
            }
            Payment payment = new Payment(0,transaction.getCustomer_name(),transaction.getContact_number(),transactions.get(0).getId(),id,""+transaction.getOrder_date().getYear()+"-"+transaction.getOrder_date().getMonthValue()+"-"+transaction.getOrder_date().getDayOfMonth(),Double.parseDouble(change),Double.parseDouble(amount),paymentId,"",0);
            this.getDBManager().query.savePayment(payment, loggedInUserInfo);
    	}
    	
    	this.borderPaneRootSwitcher.goBack_BP(3);
        
    }

    @FXML
    private void goBack() {
        System.out.println("going back...");
        this.borderPaneRootSwitcher.goBack_BP();
    }
}

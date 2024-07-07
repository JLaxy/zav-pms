package controllers.transactions;

import java.util.List;

import controllers.ParentController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import models.schemas.User;
import models.transactions.AddPaymentModel;
import models.transactions.CashPaymentPopUpModel;

public class CashPaymentPopUpController extends ParentController{
	 @FXML
	    private Button addPaymentButton, cancelButton;

	    @FXML
	    private ComboBox<String> paymentTypeCBox;
	    @FXML
	    private TextField changeField;
	    @FXML
	    private TextField amountPaidField;
	    private CreateTransactionsController controller;

	    private CashPaymentPopUpModel model;
	    @FXML
	    public void initialize(User selectedUser,CreateTransactionsController createTransactionsController) {
	        this.model = new CashPaymentPopUpModel(this);
	        this.controller = createTransactionsController;

	        // Populate the combobox with payment types from the database
	        loadPaymentTypes();
	    }

	    private void loadPaymentTypes() {
	    	System.out.println("im here");
	        List<String> paymentTypes = model.getPaymentTypes();
	        paymentTypes.forEach((String value) -> System.out.print(value));;
	        paymentTypeCBox.getItems().addAll(paymentTypes);
	    }

	    @FXML
	    private void addpayment() {
	        String selectedPaymentType = paymentTypeCBox.getValue();
	        if (selectedPaymentType != null) {
	            System.out.println("Selected mode of payment: " + selectedPaymentType);
	            // Add your logic to handle the selected payment type
	            // For example, update the parent controller or close the dialog
	            
	            this.controller.setSelectedPaymentType(selectedPaymentType);
	            this.controller.setChange(changeField.getText());
	            this.controller.setAmount(amountPaidField.getText());

	            this.borderPaneRootSwitcher.exitPopUpDialog();
	        } else {
	            // Handle the case where no payment type is selected
	            System.out.println("No payment type selected.");
	        }
	    }

	    @FXML
	    private void cancel() {
	        this.borderPaneRootSwitcher.exitPopUpDialog();
	    }
}

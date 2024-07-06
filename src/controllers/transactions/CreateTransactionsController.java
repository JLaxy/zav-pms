package controllers.transactions;

import controllers.ParentController;
import enums.ScreenPaths;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import models.schemas.OrderProduct;
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

    @FXML
    public void initialize(ObservableList<OrderProduct> orderList) {
        this.model = new CreateTransactionsModel(this);
        this.listOfOrders = orderList;

        // Calculate and display the total transaction cost and required downpayment
        double totalCost = calculateTotalTransactionCost();
        double downpayment = calculateRequiredDownpayment(totalCost);

        totalTransactionLabel.setText(String.format("%.2f", totalCost));
        requiredDownpaymentLabel.setText(String.format("%.2f", downpayment));
    }

    private double calculateTotalTransactionCost() {
        double totalCost = 0.0;
        for (OrderProduct product : listOfOrders) {
            totalCost += product.getAmount();
        }
        return totalCost;
    }

    private double calculateRequiredDownpayment(double totalCost) {
        return totalCost * 0.50;
    }

    public void setSelectedPaymentType(String paymentType) {
        this.selectedPaymentType = paymentType;
        System.out.println("Payment type set to: " + paymentType);
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
        System.out.println("Save");
    }

    @FXML
    private void goBack() {
        System.out.println("going back...");
        this.borderPaneRootSwitcher.goBack_BP();
    }
}

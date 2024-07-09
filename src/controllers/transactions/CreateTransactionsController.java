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
import models.schemas.OrderProduct;
import models.schemas.OrderedProduct;
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
    private Double change, amount;

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
        this.change = Double.valueOf(change);
    }

    public String getAmount() {
        return String.valueOf(amount);
    }

    public void setAmount(String amount) {
        // this.amount = amount;
        // double downPayment=Double.parseDouble(requiredDownpaymentLabel.getText());
        // requiredDownpaymentLabel.setText(downPayment-Double.parseDouble(amount)+"");

        this.amount = Double.parseDouble(amount) - this.change;
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
        Transaction transaction = new Transaction(0, customerNameField.getText(), LocalDateTime.now(),
                targetDate.getValue().atStartOfDay(), contactNumberField.getText(),
                transactionTypeCBox.getItems().indexOf(transactionTypeCBox.getValue()), calculateTotalDiscountAmount(),
                false, calculateTotalTransactionCost(), null, calculateTotalTransactionCost() - this.amount);
        this.model.saveTransaction(transaction, loggedInUserInfo);
        ObservableList<Transaction> transactions = this.getDBManager().query
                .getTransactions(customerNameField.getText());
        for (OrderProduct order : this.listOfOrders) {
            this.getDBManager().query.saveOrder(
                    new OrderedProduct(0, transactions.get(0).getId(),
                            // TODO: asdasdaasda
                            // THIS MUST BE PRODUCT_ID
                            this.getDBManager().query.getProductNameId(order.getProductName()), order.getQuantity(),
                            0,
                            this.zavPMSDB.query.getStockProductTypeIDByProductName(order.getProductName()), ""),
                    this.loggedInUserInfo);
        }
    }

    @FXML
    private void goBack() {
        System.out.println("going back...");
        this.borderPaneRootSwitcher.goBack_BP();
    }
}

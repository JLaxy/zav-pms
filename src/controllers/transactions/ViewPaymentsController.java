package controllers.transactions;

import controllers.ParentController;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import models.helpers.NumberHelper;
import models.schemas.Payment;
import models.transactions.ViewPaymentsModel;

public class ViewPaymentsController extends ParentController {
    @FXML
    private Label customerNameLabel, contactNumLabel, paymentTypeLabel, paymentDateLabel, modeOfPaymentLabel,
            referenceNumberLabel, ewalletVendorLabel, amountPaidLabel, changeLabel;
    @FXML
    private TextField searchField;
    @FXML
    private TableView<Payment> paymentsTable;
    @FXML
    private TableColumn<Payment, String> paymentTypeCol, customerNameCol, datePaidCol;
    @FXML
    private TableColumn<Payment, Double> paidCol;
    @FXML
    private ScrollPane paymentDetailsScrollPane;

    private Payment selectedPayment;
    private ObservableList<Payment> retrievedPayments;
    private ViewPaymentsModel model;

    @FXML
    public void initialize() {
        this.model = new ViewPaymentsModel(this);
        this.configureOnClick();
        this.configureTable();
    }

    public void retrievePayments() {
        this.borderPaneRootSwitcher.showLoadingScreen_BP();
        Task<Void> paymentRetriever = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                retrievedPayments = model
                        .retrievePayments(searchField.getText().isBlank() ? null : searchField.getText());
                Platform.runLater(() -> paymentsTable.setItems(retrievedPayments));
                return null;
            }
        };

        paymentRetriever.setOnRunning((e) -> {
            this.paymentDetailsScrollPane.setVisible(false);
        });
        paymentRetriever.setOnSucceeded(e -> this.borderPaneRootSwitcher.exitLoadingScreen_BP());

        Thread myThread = new Thread(paymentRetriever);
        myThread.setDaemon(true);
        myThread.start();
    }

    @FXML
    private void search(ActionEvent e) {
        System.out.println("searching...");
        this.retrievePayments();
    }

    @FXML
    private void goBack(ActionEvent e) {
        this.borderPaneRootSwitcher.goBack_BP();
    }

    // Configure onclick function on tableview rows
    private void configureOnClick() {
        this.paymentsTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Payment>() {
            @Override
            public void changed(ObservableValue<? extends Payment> arg0, Payment arg1, Payment arg2) {
                if (paymentsTable.getSelectionModel().getSelectedItem() != null) {
                    selectedPayment = paymentsTable.getSelectionModel().getSelectedItem();

                    customerNameLabel.setText(selectedPayment.getCustomer_name());
                    contactNumLabel.setText(selectedPayment.getContact_number());
                    paymentTypeLabel.setText(selectedPayment.getPayment_type_id_string());
                    paymentDateLabel.setText(selectedPayment.getDate_paid_formatted());
                    modeOfPaymentLabel.setText(selectedPayment.getMode_of_payment_id_string());
                    referenceNumberLabel.setText(selectedPayment.getReferenceNumber());
                    ewalletVendorLabel.setText(selectedPayment.getEwallet_vendor_id_string());
                    amountPaidLabel.setText(NumberHelper.toTwoDecimalPlaces(selectedPayment.getPaid()));
                    changeLabel.setText(NumberHelper.toTwoDecimalPlaces(selectedPayment.getChange()));

                    paymentDetailsScrollPane.setVisible(true);
                }
            }
        });
    }

    private void configureTable() {
        datePaidCol.setCellValueFactory(new PropertyValueFactory<Payment, String>("date_paid"));
        paymentTypeCol.setCellValueFactory(new PropertyValueFactory<Payment, String>("payment_type_id_string"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<Payment, String>("customer_name"));
        paidCol.setCellValueFactory(new PropertyValueFactory<Payment, Double>("paid"));

        this.paymentsTable.getColumns().forEach(e -> {
            e.setReorderable(false);
        });
    }
}

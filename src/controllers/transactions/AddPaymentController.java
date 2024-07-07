package controllers.transactions;

import controllers.ParentController;
import enums.ScreenPaths;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import models.schemas.User;
import models.transactions.AddPaymentModel;

import java.util.List;

public class AddPaymentController extends ParentController {

    @FXML
    private Button selectButton, cancelButton;

    @FXML
    private ComboBox<String> paymentTypeCBox;
    @FXML
    private TextField Change;
    @FXML
    private TextField AmountPaid;

    private CreateTransactionsController controller;
    private AddPaymentModel model;

    @FXML
    public void initialize(User selectedUser, CreateTransactionsController createTransactionsController) {
        this.model = new AddPaymentModel(this);
        this.controller = createTransactionsController;

        // Populate the combobox with payment types from the database
        loadPaymentTypes();
    }

    private void loadPaymentTypes() {
        List<String> paymentTypes = model.getPaymentTypes();
        paymentTypeCBox.getItems().addAll(paymentTypes);
    }

    @FXML
    private void select() {
        String selectedPaymentType = paymentTypeCBox.getValue();
        if (selectedPaymentType != null) {
            System.out.println("Selected mode of payment: " + selectedPaymentType);
            // Add your logic to handle the selected payment type
            // For example, update the parent controller or close the dialog
            
            this.controller.setSelectedPaymentType(selectedPaymentType);
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

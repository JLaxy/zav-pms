package controllers.reusables;

import javax.swing.JOptionPane;

import controllers.ParentController;
import controllers.inventory.SelectBeverageDecreaseController;
import controllers.inventory.ViewBeverageProductController;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import models.helpers.PopupDialog;
import models.reusables.SetBeverageDecreaseQuantityModel;

public class SetBeverageDecreaseQuantityController extends ParentController {

    @FXML
    private ComboBox<String> reductionTypeCBox;
    @FXML
    private Spinner<Integer> quantitySpinner;

    private SetBeverageDecreaseQuantityModel model;
    private SelectBeverageDecreaseController selectBeverageDecreaseController;
    private double limitQuantity;

    @FXML
    public void initialize(SelectBeverageDecreaseController selectBeverageDecreaseController, double limitQuantity) {
        this.model = new SetBeverageDecreaseQuantityModel(this);
        this.selectBeverageDecreaseController = selectBeverageDecreaseController;
        this.limitQuantity = limitQuantity;
        this.configureCBox();
        this.configureSpinner();
    }

    private void configureSpinner() {
        // Configuring quantity spinner
        this.quantitySpinner
                .setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000000000, 1));
        quantitySpinner.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^\\d+(\\.\\d*)?$")) {
                quantitySpinner.getEditor().setText(oldValue);
            }
        });
    }

    private void configureCBox() {
        Task<Void> reductionTypeRetriever = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                ObservableList<String> retrieved = model.retrieveReductionTypes();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        reductionTypeCBox.setItems(retrieved);
                        reductionTypeCBox.getSelectionModel().selectFirst();
                    }
                });
                return null;
            }
        };

        reductionTypeRetriever.setOnRunning(e -> this.borderPaneRootSwitcher.showLoadingScreen_BP());
        reductionTypeRetriever.setOnSucceeded(e -> this.borderPaneRootSwitcher.exitLoadingScreen_BP());

        Thread worker = new Thread(reductionTypeRetriever);
        worker.setDaemon(true);
        worker.start();

    }

    @FXML
    private void confirm(ActionEvent e) {
        // Show confirmation dialog
        if (PopupDialog
                .confirmOperationDialog("Do you wish to continue? this cannot be undone!") != JOptionPane.YES_OPTION)
            return;

        if (this.quantitySpinner.getValue() > this.limitQuantity) {
            PopupDialog.showCustomErrorDialog("Quantity cannot be greater than current quantity!");
            return;
        }

        this.borderPaneRootSwitcher.exitPopUpDialog();
        this.selectBeverageDecreaseController.confirmDecrease(this.quantitySpinner.getValue(),
                this.reductionTypeCBox.getValue());

    }

    @FXML
    private void cancel(ActionEvent e) {
        this.borderPaneRootSwitcher.goBack_BP();
    }

}

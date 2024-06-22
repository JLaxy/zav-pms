package controllers.inventory;

import controllers.ParentController;
import enums.ScreenPaths;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import models.helpers.PopupDialog;
import models.helpers.StringHelper;
import models.inventory.RegisterNewBeverageVariantModel;

public class RegisterNewBeverageVariantController extends ParentController {

    @FXML
    private ComboBox<String> beverageProductCBox;

    private RegisterNewBeverageVariantModel model;

    @FXML
    public void initialize() {
        this.model = new RegisterNewBeverageVariantModel(this);
    }

    // Initializes Combo Boxes
    public void initalizeComboBox() {
        getBeverageProducts();
    }

    // Retrieves beverage products on database
    private void getBeverageProducts() {
        try {
            // Show loading Screen
            this.borderPaneRootSwitcher.showLoadingScreen_BP();

            // Create thread
            Service<Void> beverageRetriever = new Service<Void>() {
                @Override
                protected Task<Void> createTask() {
                    return new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            // Get retrieved value
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    beverageProductCBox.setItems(model.getBeverageProducts());
                                }
                            });
                            return null;
                        }
                    };
                }
            };
            beverageRetriever.setOnSucceeded(e -> {
                // Exit Loading Screen
                borderPaneRootSwitcher.exitLoadingScreen_BP();
                this.beverageProductCBox.getSelectionModel().selectFirst();
            });
            beverageRetriever.start();
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
        }
    }

    @FXML
    public void newBeverage(ActionEvent e) {
        this.initializePopUpDialog(ScreenPaths.Paths.REGISTER_NEW_BEVERAGE_POPUP.getPath(), loggedInUserInfo);
    }

    @FXML
    public void goBack(ActionEvent e) {
        this.borderPaneRootSwitcher.goBack_BP();
    }

    @FXML
    public void select(ActionEvent e) {
        RegisterNewBeverageVariantDetailsController controller = (RegisterNewBeverageVariantDetailsController) this
                .initializeNextScreen_BP(ScreenPaths.Paths.REGISTER_NEW_BEVERAGE_VARIANT_DETAILS.getPath(),
                        loggedInUserInfo, "NEW BEVERAGE");
        controller.initialize(StringHelper.toTitleCase(beverageProductCBox.getSelectionModel().getSelectedItem()),
                false);
    }
}

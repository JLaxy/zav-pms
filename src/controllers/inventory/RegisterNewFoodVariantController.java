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
import models.inventory.RegisterNewFoodVariantModel;

public class RegisterNewFoodVariantController extends ParentController {
    @FXML
    private ComboBox<String> foodProductCBox;

    private RegisterNewFoodVariantModel model;

    @FXML
    public void initialize() {
        this.model = new RegisterNewFoodVariantModel(this);
    }

    public void configureComboBox() {
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
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    // Get retrieved value
                                    foodProductCBox.setItems(model.getFoodProducts());
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
                this.foodProductCBox.getSelectionModel().selectFirst();
            });
            beverageRetriever.start();
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
        }
    }

    @FXML
    private void select(ActionEvent e) {
        RegisterNewFoodVariantDetailsController controller = (RegisterNewFoodVariantDetailsController) this
                .initializeNextScreen_BP(ScreenPaths.Paths.REGISTER_NEW_FOOD_VARIANT_DETAILS.getPath(),
                        loggedInUserInfo,
                        "NEW FOOD VARIANT");
        controller.initialize(StringHelper.toTitleCase(foodProductCBox.getValue()), false);
    }

    @FXML
    private void goBack(ActionEvent e) {
        this.borderPaneRootSwitcher.goBack_BP();
    }

    @FXML
    private void newFood(ActionEvent e) {
        this.initializePopUpDialog(ScreenPaths.Paths.REGISTER_NEW_FOOD_POPUP.getPath(), loggedInUserInfo);
    }
}

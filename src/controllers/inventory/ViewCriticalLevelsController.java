package controllers.inventory;

import controllers.ParentController;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import models.helpers.UIElementsBuilderHelper;
import models.inventory.ViewCriticalLevelsModel;
import models.schemas.DeprecatedItem;

public class ViewCriticalLevelsController extends ParentController {

    @FXML
    private VBox criticalLevelItemsVBox;

    private ViewCriticalLevelsModel model;
    private ObservableList<DeprecatedItem> criticalLevelItems;

    @FXML
    private void initialize() {
        this.model = new ViewCriticalLevelsModel(this);
    }

    @FXML
    private void close() {
        this.borderPaneRootSwitcher.exitPopUpDialog();
    }

    // Retrieving items in critical level in database
    public void retrieveCriticalLevelItems() {
        Task<Void> critItemsRetriever = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                criticalLevelItems = model.getItemsInCriticalLevels();
                return null;
            }
        };

        critItemsRetriever.setOnRunning(e -> {
            this.borderPaneRootSwitcher.showLoadingScreen_BP();
        });

        critItemsRetriever.setOnSucceeded(e -> {
            showItemsOnScreen();
            this.borderPaneRootSwitcher.exitLoadingScreen_BP();
        });

        Thread critItemsRetrieverThread = new Thread(critItemsRetriever);
        critItemsRetrieverThread.setDaemon(true);
        critItemsRetrieverThread.start();
    }

    // Displays all of the retrieved elements on screen
    private void showItemsOnScreen() {
        for (DeprecatedItem criticalItem : criticalLevelItems) {
            // Create Labels
            Label inventoryItemNameLabel = new Label(criticalItem.getInventory_item());
            inventoryItemNameLabel.setAlignment(Pos.CENTER_LEFT);
            inventoryItemNameLabel.setPrefWidth(500d);
            Label remainingLabel = new Label(String.valueOf(criticalItem.getQuantity()));
            remainingLabel.setAlignment(Pos.CENTER);
            remainingLabel.setPrefWidth(200d);
            Label criticalLevelLabel = new Label(String.valueOf(criticalItem.getCritical_level()));
            criticalLevelLabel.setAlignment(Pos.CENTER_RIGHT);
            criticalLevelLabel.setPrefWidth(200d);

            HBox myHBox = new HBox();
            myHBox.getChildren().addAll(inventoryItemNameLabel, remainingLabel, criticalLevelLabel);
            UIElementsBuilderHelper.applyPopUpDialogLabelStyles(myHBox.getChildren());

            // Add to VBox
            this.criticalLevelItemsVBox.getChildren()
                    .add(myHBox);
        }
    }
}

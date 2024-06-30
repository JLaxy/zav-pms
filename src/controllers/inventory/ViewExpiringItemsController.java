package controllers.inventory;

import controllers.ParentController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import models.helpers.DateHelper;
import models.helpers.UIElementsBuilderHelper;
import models.inventory.ViewExpiringItemsModel;
import models.schemas.DeprecatedItem;

public class ViewExpiringItemsController extends ParentController {

    @FXML
    private VBox expiringItemsVBox, expiredItemsVBox;
    @FXML
    private Label nearlyExpItemsLabel, expItemsLabel;

    private ViewExpiringItemsModel model;
    private ObservableList<DeprecatedItem> expiringItemsList, expiredItemsList;

    @FXML
    public void initialize() {
        this.model = new ViewExpiringItemsModel(this);
    }

    public void retrieveExpiringItems() {
        this.expiringItemsList = FXCollections.observableArrayList();
        this.expiredItemsList = FXCollections.observableArrayList();

        borderPaneRootSwitcher.showLoadingScreen_BP();
        Service<Void> expiringItemsRetriever = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        // Retrieving Expired Items from database in ObservableList
                        expiredItemsList = FXCollections.observableArrayList();
                        expiredItemsList.addAll(model.getDeprecatedItems("EXPIRED"));

                        expiringItemsList = FXCollections.observableArrayList();
                        expiringItemsList.addAll(model.getDeprecatedItems("EXPIRING"));

                        for (DeprecatedItem expiredItem : expiredItemsList) {
                            // Generate HBoxentries to be put in VBOX
                            HBox expiredItemEntry = getExpiryEntry(expiredItem);
                            UIElementsBuilderHelper.applyPopUpDialogLabelStyles(expiredItemEntry.getChildren());
                            // Add to UI
                            Platform.runLater(() -> expiredItemsVBox.getChildren().add(expiredItemEntry));
                        }

                        for (DeprecatedItem expiredItem : expiringItemsList) {
                            // Generate HBoxentries to be put in VBOX
                            HBox expiredItemEntry = getExpiryEntry(expiredItem);
                            UIElementsBuilderHelper.applyPopUpDialogLabelStyles(expiredItemEntry.getChildren());
                            // Add to UI
                            Platform.runLater(() -> expiringItemsVBox.getChildren().add(expiredItemEntry));
                        }

                        // Hide Labels if empty
                        Platform.runLater(() -> {
                            if (expiringItemsList.size() == 0)
                                nearlyExpItemsLabel.setVisible(false);
                            if (expiredItemsList.size() == 0)
                                expItemsLabel.setVisible(false);
                        });

                        return null;
                    }
                };
            }
        };
        expiringItemsRetriever.setOnSucceeded(e -> {
            borderPaneRootSwitcher.exitLoadingScreen_BP();
        });
        expiringItemsRetriever.start();
    }

    @FXML
    public void close(ActionEvent e) {
        this.borderPaneRootSwitcher.exitPopUpDialog();
    }

    // Returns an expiry entry
    private HBox getExpiryEntry(DeprecatedItem deprecatedItem) {
        HBox entryHBox = new HBox();
        Label itemLabel = new Label(deprecatedItem.getInventory_item());
        Label expiryLabel = new Label(DateHelper.dateToFormattedDate(deprecatedItem.getExpiry_date()));
        Label quantityLabel = new Label(deprecatedItem.getQuantity() + "x");

        // Applying config
        itemLabel.setPrefWidth(500d);
        itemLabel.setAlignment(Pos.CENTER_LEFT);
        quantityLabel.setPrefWidth(200d);
        quantityLabel.setAlignment(Pos.CENTER_LEFT);
        expiryLabel.setPrefWidth(200d);
        expiryLabel.setAlignment(Pos.CENTER_RIGHT);

        entryHBox.getChildren().addAll((Node) quantityLabel, (Node) itemLabel, (Node) expiryLabel);
        return entryHBox;
    }

}

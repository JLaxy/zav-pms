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
import models.inventory.ViewExpiringItemsModel;
import models.schemas.ExpiringItems;

public class ViewExpiringItemsController extends ParentController {

    @FXML
    private VBox expiringItemsVBox, expiredItemsVBox;

    private ViewExpiringItemsModel model;
    private ObservableList<ExpiringItems> expiringItemsList, expiredItemsList;

    @FXML
    public void initialize() {
        this.model = new ViewExpiringItemsModel(this);
    }

    public void retrieveExpiringItems() {
        borderPaneRootSwitcher.showLoadingScreen_BP();
        Service<Void> expiringItemsRetriever = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        expiringItemsList = FXCollections.observableArrayList();
                        expiringItemsList.addAll(model.getExpiringBeverages());

                        expiredItemsList = FXCollections.observableArrayList();
                        expiredItemsList = model.getExpiredBeverages();

                        expiringItemsList.addAll(model.getExpiringStock());
                        expiredItemsList.addAll(model.getExpiredStock());

                        for (ExpiringItems expiringItem : expiringItemsList) {
                            // Get hbox entry
                            HBox hBoxEntry = getExpiryEntry(String.valueOf(expiringItem.getQuantity()),
                                    expiringItem.getInventory_item(),
                                    DateHelper.dateToFormattedDate(expiringItem.getExpiry_date()));
                            // Apply styles to label
                            applyLabelStyles(hBoxEntry.getChildren());

                            // Add to screen thread
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    // Add to screen
                                    expiringItemsVBox.getChildren()
                                            .add(hBoxEntry);
                                }
                            });
                        }

                        for (ExpiringItems expiredItem : expiredItemsList) {
                            // Get hbox entry
                            HBox hBoxEntry = getExpiryEntry(String.valueOf(expiredItem.getQuantity()),
                                    expiredItem.getInventory_item(),
                                    DateHelper.dateToFormattedDate(expiredItem.getExpiry_date()));
                            // Apply styles to label
                            applyLabelStyles(hBoxEntry.getChildren());

                            // Add to screen thread
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    // Add to screen
                                    expiredItemsVBox.getChildren()
                                            .add(hBoxEntry);
                                }
                            });
                        }

                        return null;
                    }
                };
            }
        };
        expiringItemsRetriever.setOnSucceeded(e -> {
            borderPaneRootSwitcher.exitLoadingScreen_BP();
        });
        expiringItemsRetriever.start();
        System.out.println("started");
    }

    @FXML
    public void close(ActionEvent e) {
        this.borderPaneRootSwitcher.exitPopUpDialog();
    }

    // Returns an expiry entry
    private HBox getExpiryEntry(String quantity, String itemName, String expirydate) {
        HBox entryHBox = new HBox();
        Label itemLabel = new Label(itemName);
        Label expiryLabel = new Label(expirydate);
        Label quantityLabel = new Label(quantity + "x");

        // Applying config
        itemLabel.setPrefWidth(500d);
        itemLabel.setAlignment(Pos.CENTER_LEFT);
        quantityLabel.setPrefWidth(300d);
        quantityLabel.setAlignment(Pos.CENTER_LEFT);
        expiryLabel.setPrefWidth(600d);
        expiryLabel.setAlignment(Pos.CENTER_RIGHT);

        entryHBox.getChildren().addAll((Node) quantityLabel, (Node) itemLabel, (Node) expiryLabel);
        return entryHBox;
    }

    // Applying styles to label
    private void applyLabelStyles(ObservableList<Node> labelsList) {
        for (Node node : labelsList) {
            node.setStyle("-fx-font-size: 20");
        }
    }

}

package controllers;

import javafx.fxml.FXML;

public class OrderController extends ParentController {
    @FXML
    private void createOrder() {
        System.out.println("creating order...");
        showReferences();
    }

    @FXML
    private void viewOrders() {
        System.out.println("viewOrders");
    }

    @FXML
    private void showBackorders() {
        System.out.println("showBackorders");
    }

    @FXML
    private void goBack() {
        System.out.println("going back...");
        this.borderPaneRootSwitcher.goBack_BP();
    }
}

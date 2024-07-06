package controllers.order;

import controllers.ParentController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class SelectFoodOrderController extends ParentController {
    @FXML
    private void confirm(ActionEvent e) {
        // - After selecting ordered food product, ask how many will be made; put error
        // checking such as invalid input, negative quantity, too much quantity than
        // required
        // - prevent user from increasing if stock required to make ordered food product
        // is insufficient
        System.out.println("Confirming..");

        // - for each required stock to make the food product, record on
        // stock_product_reduction; make sure to use nearest expiring stock of that
        // product that
        // still has stock (check stock_product_rection if quantity of specific
        // stock_product_expenses row has already been depleted; be able to switch to
        // next nearest oldest stock if insufficient)
        // - use appropriate ordered_products_id matching selected food product.

    }

    @FXML
    private void goBack(ActionEvent e) {
        this.borderPaneRootSwitcher.goBack_BP();
    }
}

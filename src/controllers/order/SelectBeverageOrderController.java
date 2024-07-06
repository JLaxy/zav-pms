package controllers.order;

import controllers.ParentController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class SelectBeverageOrderController extends ParentController {
    @FXML
    private void confirm(ActionEvent e) {
        // - After selecting ordered beverage product, ask how many to allocate; put
        // error
        // checking such as invalid input, negative quantity, too much quantity than
        // required
        // - prevent user from decreasing if avaialable beverage is insufficient
        System.out.println("Confirming..");

        // record on
        // stock_product_reduction; make sure to use nearest expiring stock of that
        // product that
        // still has stock (check stock_product_rection if quantity of specific
        // stock_product_expenses row has already been depleted; be able to switch to
        // next nearest old beverage if insufficient)
        // - use appropriate ordered_products_id matching selected food product.

    }

    @FXML
    private void goBack(ActionEvent e) {
        this.borderPaneRootSwitcher.goBack_BP();
    }
}

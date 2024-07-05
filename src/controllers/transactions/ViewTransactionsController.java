package controllers.transactions;

import controllers.ParentController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class ViewTransactionsController extends ParentController {

    // private TableColumn<>

    @FXML
    private void initialize() {
        this.configureTable();
    }

    @FXML
    private void goBack(ActionEvent e) {
        this.borderPaneRootSwitcher.goBack_BP();
    }

    @FXML
    private void configureTable() {
        // foodTable.getColumns().forEach(e -> {
        // e.setReorderable(false);
        // });
    }

    @FXML
    private void search(ActionEvent e) {
        System.out.println("Searching...");
    }

    @FXML
    private void editTransaction(ActionEvent e) {
        System.out.println("edit trans");
    }
}

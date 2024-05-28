package controllers.report;

import controllers.ParentController;
import javafx.fxml.FXML;

public class ManualGenerateReportController extends ParentController {
    @FXML
    private void goBack() {
        System.out.println("going back...");
        this.borderPaneRootSwitcher.goBack_BP();
    }

    @FXML
    private void periodicsales() {
        System.out.println("Periodic Sales");
    }

    @FXML
    private void reorderqueue() {
        System.out.println("Reorder Queue");
    }

    @FXML
    private void preorderstock() {
        System.out.println("Preorder Stock");
    }
}

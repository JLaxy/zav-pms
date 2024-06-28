package controllers.order;

import controllers.ParentController;
import enums.ScreenPaths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class SelectSizeController extends ParentController {
    
    @FXML
    private void select() {
        initializeNextScreen_BP(ScreenPaths.Paths.VIEW_BACKORDER.getPath(), this.loggedInUserInfo,
        "VIEW BACKORDER");
        System.out.println("showBackorders");
    }

    @FXML
    private void cancel(ActionEvent e) {
        this.borderPaneRootSwitcher.exitPopUpDialog();
    }

    public void initialize() {
        // this.model = new SelectSizeModel;
    }
}

package controllers.about;

import controllers.ParentController;
import enums.ScreenPaths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;


public class AboutController extends ParentController {

    @FXML
    private void goBack(ActionEvent e) {
        this.borderPaneRootSwitcher.goBack_BP();
    }

}

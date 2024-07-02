package controllers.help;

import controllers.ParentController;
import enums.ScreenPaths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;


public class HelpController extends ParentController {

    @FXML
    private void goBack(ActionEvent e) {
        this.borderPaneRootSwitcher.goBack_BP();
    }

    @FXML
    private void general(){
        this.initializeNextScreen_BP(ScreenPaths.Paths.GENERAL.getPath(), this.loggedInUserInfo,
                    "GENERAL");
        System.out.println("General");
    }

    @FXML
    private void faqs(){
        this.initializeNextScreen_BP(ScreenPaths.Paths.FAQS.getPath(), this.loggedInUserInfo,
                    "FAQs");
        System.out.println("FAQs");
    }

    @FXML
    private void howto(){
        System.out.println("How To");
    }
}

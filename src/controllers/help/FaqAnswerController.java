package controllers.help;

import controllers.ParentController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class FaqAnswerController extends ParentController {

    @FXML
    private Label answerLabel;

    @FXML
    public void initialize(String answer) {
        this.answerLabel.setText(answer);
    }

    @FXML
    private void goBack() {
        this.borderPaneRootSwitcher.goBack_BP();
    }
}

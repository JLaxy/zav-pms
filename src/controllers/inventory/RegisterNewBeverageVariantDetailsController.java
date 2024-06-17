package controllers.inventory;

import controllers.ParentController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class RegisterNewBeverageVariantDetailsController extends ParentController {

    @FXML
    private Rectangle sliderRect;
    @FXML
    private Label literLabel, mililiterLabel;

    private String productName, unitMeasurement = "mL";

    @FXML
    public void initialize(String productName) {
        this.productName = productName;
    }

    @FXML
    private void goBack(ActionEvent e) {
        this.borderPaneRootSwitcher.goBack_BP();
    }

    @FXML
    private void register(ActionEvent e) {
        System.out.println("registering...");
    }

    @FXML
    private void toggleMeasurement() {
        if (this.unitMeasurement.compareTo("mL") == 0)
            this.unitMeasurement = "L";
        else if (this.unitMeasurement.compareTo("L") == 0)
            this.unitMeasurement = "mL";

        System.out.println(this.unitMeasurement);
        updateButtonState();
    }

    // Updates state of custom button element according to selected unitmeasurement
    private void updateButtonState() {
        if (this.unitMeasurement.compareTo("mL") == 0) {
            this.mililiterLabel.setTextFill(Color.BLACK);
            this.literLabel.setTextFill(Color.WHITE);
            this.sliderRect.setLayoutX(1103);
            this.sliderRect.setLayoutY(166);
            return;
        }
        this.mililiterLabel.setTextFill(Color.WHITE);
        this.literLabel.setTextFill(Color.BLACK);
        this.sliderRect.setLayoutX(1174);
        this.sliderRect.setLayoutY(166);
        return;
    }

}
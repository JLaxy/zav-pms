package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import models.ForgotPasswordModel;
import controllers.PasswordQuestionController;

/*
 * ForgotPasswordController
 */
public class ForgotPasswordController extends ParentController {
    ForgotPasswordModel model;

    @FXML
    Button nextButton;

    @FXML
    TextField unameField;

    @FXML
    public void initialize() {
        this.model = new ForgotPasswordModel(this);
    }

    // Next Action
    public void nextAction(ActionEvent e) {
        String uName = unameField.getText();
        if (this.model.doesUsernameExist(uName)) {
            // Navigate to next screen
            PasswordQuestionController nextController = (PasswordQuestionController) initializeNextScreen(
                    "../views/fxmls/PasswordQuestionView.fxml", loggedInUser);
            // Retrieves question details of user (uname, question, answer)
            String[] questionDetails = this.model.getUserQuestions(uName);
            // Passing question details to next controller
            nextController.question = questionDetails[1];
            nextController.answer = questionDetails[2];
            // Configure controller
            nextController.configureQuestions();
        }
    }
}
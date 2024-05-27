package controllers.login;

import java.util.Map;

import controllers.ParentController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import models.login.ForgotPasswordModel;

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
        // Retrieving info of user
        Map<String, String> userInfo = this.model.getUserInfo(uName);

        // If ID is empty; then means user does not exist
        if (userInfo.get("id") != "" && Integer.valueOf(userInfo.get("id")) != 1) {
            // Navigate to next screen
            PasswordQuestionController nextController = (PasswordQuestionController) initializeNextScreen(
                    "../../views/fxmls/login/PasswordQuestionView.fxml", this.loggedInUserInfo);
            // Retrieves question details of user (uname, question, answer)
            String[] questionDetails = this.model.getUserQuestions(uName);
            // Passing question details to next controller
            nextController.setQuestionAnswer(questionDetails[1], questionDetails[2]);
            // Configure controller
            nextController.configureController(userInfo);
            // Logging Password Reset Action to Database
            this.model.logPasswordReset(userInfo.get("id"), userInfo.get("uname"));
        }
    }

    // Going back to previous screen
    public void goBack(ActionEvent e) {
        rootSwitcher.goBack();
    }
}
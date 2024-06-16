package controllers.login;

import controllers.ParentController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import models.helpers.PopupDialog;
import models.login.ForgotPasswordModel;
import models.schemas.User;

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
        User userInfo = this.model.getUserInfo(uName);

        // If ID is empty; then means user does not exist

        if (userInfo.getId() != 0 && userInfo.getId() != 1 || userInfo.getUname() != null) {
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
            this.model.logPasswordReset(String.valueOf(userInfo.getId()), userInfo.getUname());
            return;
        }

        PopupDialog.showCustomErrorDialog("User \"" + uName + "\" does not exist!");
    }

    // Going back to previous screen
    public void goBack(ActionEvent e) {
        rootSwitcher.goBack();
    }
}
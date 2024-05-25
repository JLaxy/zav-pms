package controllers;

import java.util.Map;

import javax.swing.JOptionPane;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import models.PasswordQuestionModel;
import models.helpers.PopupDialog;

public class PasswordQuestionController extends ParentController {
    @FXML
    private Label questionLabel, errorLabel;
    @FXML
    private TextField answerField;

    private String question, answer;
    private Map<String, String> userInfo;

    private PasswordQuestionModel model;

    @FXML
    public void initialize() {
        this.model = new PasswordQuestionModel(this);
    }

    // Updates question and answer
    public void setQuestionAnswer(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    // Initializes screen; displays question
    public void configureController(Map<String, String> userInfo) {
        this.userInfo = userInfo;
        // Hide error label
        this.errorLabel.setVisible(false);
        // Set question on view
        this.questionLabel.setText(this.question);
    }

    // Cancel Button On-Action
    public void cancel() {
        // Confirmation Diaglog
        if (PopupDialog.cancelOperationDialog() == JOptionPane.YES_OPTION) {
            // Logging to database
            this.model.logAction(userInfo.get("id"), userInfo.get("uname"));
            this.rootSwitcher.goBack(2);
        }
    }

    // Verify Button On-Action
    public void verifyAnswer() {
        // If matches answer
        if (answerField.getText().compareTo(this.answer) == 0) {
            this.errorLabel.setVisible(false);
            System.out.println("correct!");
            initializeNextScreen(
                    "../views/fxmls/NewPasswordView.fxml", userInfo);
            // Else, show error label
        } else
            this.errorLabel.setVisible(true);
    }
}

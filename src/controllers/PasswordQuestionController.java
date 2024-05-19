package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PasswordQuestionController extends ParentController {
    @FXML
    private Label questionLabel;

    String question;
    String answer;

    public void configureQuestions() {
        this.questionLabel.setText(this.question);
    }
}

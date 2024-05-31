package controllers;

import models.schemas.User;

public class StartController extends ParentController {

    // Get Started Button Action
    public void getStartedAction() {
        testFunc();
        initializeNextScreen("../views/fxmls/login/LoginView.fxml", new User());
    }

    // Debug function
    private void testFunc() {

    }
}

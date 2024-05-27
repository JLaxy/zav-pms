package controllers;

import java.util.Map;

public class StartController extends ParentController {

    // Get Started Button Action
    public void getStartedAction() {
        testFunc();
        initializeNextScreen("../views/fxmls/login/LoginView.fxml", Map.of("id", ""));
    }

    // Debug function
    private void testFunc() {

    }
}

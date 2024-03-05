package controllers;

public class StartController extends ParentController {

    // Get Started Button Action
    public void getStartedAction() {
        testFunc();
        initializeNextScreen("../views/fxmls/LoginView.fxml", 0);
    }

    // Debug function
    private void testFunc() {

    }
}

package controllers;

import enums.ScreenPaths;
import models.schemas.User;

public class StartController extends ParentController {

    // Get Started Button Action
    public void getStartedAction() {
        testFunc();
        initializeNextScreen(ScreenPaths.Paths.LOGIN.getPath(), new User());
    }

    // Debug function
    private void testFunc() {

    }
}

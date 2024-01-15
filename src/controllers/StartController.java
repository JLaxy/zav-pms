package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class StartController extends ParentController {

    // Get Started Button Action
    public void getStarted() {
        System.out.println("getting started...");
        try {

            // DEBUG PURPOSES
            FXMLLoader rootLoader = new FXMLLoader(getClass().getResource("../views/fxmls/LoginView.fxml"));

            // FXMLLoader can only load once
            Parent root = rootLoader.load();
            // Passing RootSwitcher instance to next controller
            ParentController nextController = rootLoader.getController();
            nextController.setRootSwitcher(this.rootSwitcher);
            // Calling Root Switcher to navigate to next view
            this.rootSwitcher.nextView(root);
        } catch (Exception e) {
            System.out.println("Error at: " + getClass());
            e.printStackTrace();
        }
    }
}

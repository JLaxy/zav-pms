package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;

public class StartController extends ParentController {
    @FXML
    Button getStartedButton;

    public void getStarted() {
        System.out.println("getting started...");
        try {
            FXMLLoader rootLoader = new FXMLLoader(getClass().getResource("../views/fxmls/LoginView.fxml"));
            // FXMLLoader can only load once
            Parent root = rootLoader.load();
            ParentController nextController = rootLoader.getController();
            nextController.setRootSwitcher(this.rootSwitcher);
            this.rootSwitcher.nextView(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

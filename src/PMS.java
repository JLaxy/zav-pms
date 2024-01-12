/*
 * Product Management System Application
 */

import controllers.ParentController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import models.helpers.RootSwitcher;

public class PMS extends Application {
    // Easy to change values
    String APP_TITLE = "Zav's Kitchen and Bar: Product Management System";
    String ICON_PATH = "file:../../assets/images/logoTransparent.png";
    int stageWidth = 1366;
    int stageHeight = 768;

    // Starting
    @Override
    public void start(Stage mainStage) throws Exception {
        try {
            // Loading GUI built on Scene Builder
            FXMLLoader rootLoader = new FXMLLoader(getClass().getResource("views/fxmls/StartView.fxml"));

            // Retrieving root
            Parent root = rootLoader.load();

            // Adding to scene
            Scene scene = new Scene(root);

            // Sets icon of application
            mainStage.getIcons().add(new Image(ICON_PATH));

            // Sets title of the Stage / Window
            mainStage.setTitle(APP_TITLE);

            // Setting Stage Dimensions
            // mainStage.setWidth(stageWidth);
            // mainStage.setHeight(stageHeight);
            mainStage.setResizable(false);
            mainStage.setScene(scene);

            // Makes stage visible
            mainStage.show();

            // Initializing Root Switcher; allows to change View
            RootSwitcher rootSwitcher = new RootSwitcher(mainStage);
            ParentController nextController = rootLoader.getController();
            nextController.setRootSwitcher(rootSwitcher);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

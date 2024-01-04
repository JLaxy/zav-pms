package views;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MainView extends Application {

    // Easy to change values
    String APP_TITLE = "Zav's Kitchen and Bar: Product Management System";
    String ICON_PATH = "file:../../assets/images/logoTransparent.png";
    int stageWidth = 1366;
    int stageHeight = 768;

    // Starting
    @Override
    public void start(Stage mainStage) throws Exception {
        // TODO Auto-generated method stub
        // Creating root node
        Group root = new Group();
        // Adding to scene
        Scene scene = new Scene(root, Color.AQUA);

        // Sets icon of application
        mainStage.getIcons().add(new Image(ICON_PATH));
        // Sets title of the Stage / Window
        mainStage.setTitle(APP_TITLE);
        mainStage.setWidth(stageWidth);
        mainStage.setHeight(stageHeight);
        mainStage.setResizable(false);
        mainStage.setScene(scene);

        // Makes stage visible
        mainStage.show();
    }

}
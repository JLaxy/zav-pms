package views;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
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
        try {
            // Creating root node
            Parent root = FXMLLoader.load(getClass().getResource("fxmls/MainView.fxml"));
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
            mainStage.setFullScreen(true);

            // Makes stage visible
            mainStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}